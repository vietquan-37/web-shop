package com.vietquan.security.controller;

import com.vietquan.security.request.AuthenticationRequest;
import com.vietquan.security.request.RefreshTokenRequest;
import com.vietquan.security.request.verifyRequest;
import com.vietquan.security.response.AuthenticationResponse;
import com.vietquan.security.service.AuthenticationService;
import com.vietquan.security.request.RegisterRequest;
import com.vietquan.security.exception.EmailAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<AuthenticationResponse>authenticate(
            @RequestBody AuthenticationRequest request
    )  {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody RefreshTokenRequest request
            ) throws IOException {
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
