package com.vietquan.security.controller;

import com.vietquan.security.exception.InvalidPasswordException;
import com.vietquan.security.exception.MisMatchPasswordException;
import com.vietquan.security.request.ChangePasswordRequest;
import com.vietquan.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;

    @PatchMapping
    public ResponseEntity<?>changePassword(
         @RequestBody ChangePasswordRequest request,
         Principal connectedUser
    ) throws InvalidPasswordException, MisMatchPasswordException {
  service.changePassword(request,connectedUser);
          return ResponseEntity.accepted().build();
    }



}
