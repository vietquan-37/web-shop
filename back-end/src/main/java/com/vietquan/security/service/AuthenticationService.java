package com.vietquan.security.service;

import com.vietquan.security.entity.Order;
import com.vietquan.security.entity.Token;
import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.Role;
import com.vietquan.security.enumPackage.TokenType;
import com.vietquan.security.exception.EmailAlreadyExistsException;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.repository.TokenRepository;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.request.AuthenticationRequest;
import com.vietquan.security.request.RefreshTokenRequest;
import com.vietquan.security.request.RegisterRequest;
import com.vietquan.security.request.verifyRequest;
import com.vietquan.security.response.AuthenticationResponse;
import com.vietquan.security.tfa.TwoFactorAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;
    private final OrderRepository orderRepository;

    public AuthenticationResponse register(RegisterRequest request) throws EmailAlreadyExistsException {
        var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).mfaEnable(request.isMfaEnable()).build();
        if (repository.findByEmail(request.getEmail()).isPresent()) {

            throw new EmailAlreadyExistsException("Email address already registered");
        }

// if the mfa enable --generate secret
        if (request.isMfaEnable()) {
          repository.save(user);
            user.setSecret(twoFactorAuthenticationService.generateNewSecret());
            createAndSaveOrder(user);

        }
        var saveUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        createAndSaveOrder(saveUser);


        return AuthenticationResponse.builder().userId(user.getId()).accessToken(jwtToken).refreshToken(refreshToken).mfaEnable(user.isMfaEnable()).secretImage(twoFactorAuthenticationService.generateQrCodeImageUri(user.getSecret())).role(user.getRole()).build();

    }

    private void createAndSaveOrder(User user) {
        Order order = new Order();
        order.setAmount(0.0);
        order.setTotalAmount(0.0);
        order.setDiscount(0.0);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
    }

    private void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserToken.isEmpty()) {
            return;
        }
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);

        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                //this trigger the authenticationProvider() to authenticate user
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        if (user.isMfaEnable()) {

            return AuthenticationResponse.builder().accessToken(null).refreshToken(null).mfaEnable(true).role(null).build();

        }
        // Generate a JWT token and refresh token for the user
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Save the refresh token to the database
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);

        // Return a response to the user containing the JWT token and refresh token
        return AuthenticationResponse.builder().userId(user.getId()).accessToken(jwtToken).refreshToken(refreshToken).role(user.getRole()).mfaEnable(false).build();

    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.getToken());
        User user = repository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(request.getToken(), user)) {

            var accessToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().userId(user.getId()).accessToken(accessToken).refreshToken(request.getToken()).build();

        } else {
            return AuthenticationResponse.builder().userId(null).accessToken(null).refreshToken(null).mfaEnable(false).build();

        }

    }


    public Object verifyCode(verifyRequest verifyRequest) {
        User user = repository.findByEmail(verifyRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException(String.format("No User found with %S", verifyRequest.getEmail())));

        if (twoFactorAuthenticationService.isOtpNotValid(user.getSecret(), verifyRequest.getCode())) {
            throw new BadCredentialsException("Code is not correct");
        } else {
            var jwtToKen = jwtService.generateToken(user);
            var refresh = jwtService.generateRefreshToken(user);

            return AuthenticationResponse.builder().userId(user.getId()).accessToken(jwtToKen).refreshToken(refresh).mfaEnable(user.isMfaEnable()).role(user.getRole()).build();
        }
    }
}
