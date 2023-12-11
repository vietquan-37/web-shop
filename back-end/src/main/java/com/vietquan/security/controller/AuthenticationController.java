package com.vietquan.security.controller;

import com.vietquan.security.exception.EmailAlreadyExistsException;
import com.vietquan.security.exception.MisMatchPasswordException;
import com.vietquan.security.exception.ResetTokenExpired;
import com.vietquan.security.request.*;
import com.vietquan.security.response.AuthenticationResponse;
import com.vietquan.security.response.ResponseMessage;
import com.vietquan.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "https://vietquan-ecommerce.netlify.app")
public class AuthenticationController {
    @Autowired
    private AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<?> register(
              @RequestBody @Valid RegisterRequest request
    ) throws EmailAlreadyExistsException {
        var response=service.register(request);
        if(request.isMfaEnable()){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/forgot")
    public ResponseEntity<ResponseMessage> forgotPassword(
            @RequestBody ForgotRequest request
    ) {
        return service.forgotPassword(request);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage>resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,@RequestParam(name = "token")String token
            ) throws MisMatchPasswordException, ResetTokenExpired {
        return service.resetPassword(request,token);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request
    )  {
        service.logout(request);
    }
    @PostMapping("/verify")
    public ResponseEntity<?>verifyCode(
           @RequestBody verifyRequest verifyRequest
    ){
        return ResponseEntity.ok(service.verifyCode(verifyRequest));
    }

}
