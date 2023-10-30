package com.vietquan.security.service;

import com.vietquan.security.entity.User;
import com.vietquan.security.exception.InvalidPasswordException;
import com.vietquan.security.exception.MisMatchPasswordException;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) throws InvalidPasswordException, MisMatchPasswordException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        //check the current password is correct

        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("wrong password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new MisMatchPasswordException("password does not match");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        repository.save(user);

    }
}
