package com.vietquan.security.controller;

import com.vietquan.security.exception.InvalidPasswordException;
import com.vietquan.security.exception.MisMatchPasswordException;
import com.vietquan.security.request.ChangeInformationRequest;
import com.vietquan.security.request.ChangePasswordRequest;
import com.vietquan.security.request.StatusRequest;
import com.vietquan.security.response.UserInfoResponse;
import com.vietquan.security.service.UserService;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;

    @PutMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> changePassword(
           @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) throws InvalidPasswordException, MisMatchPasswordException {
        service.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/changeInfo/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserInfoResponse> changeInfo(@ModelAttribute ChangeInformationRequest request,@PathVariable Integer userId) {
        UserInfoResponse result = service.changeInfo(request, userId);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/allUsers")
    public ResponseEntity<Page<UserInfoResponse>>getAllUser(@RequestParam(defaultValue = "0")int page){
        return ResponseEntity.ok(service.getAllUserInfo(page));
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponse>getUserInfo(@PathVariable Integer userId){
        return ResponseEntity.ok(service.getUserInfo(userId));
    }
    @PatchMapping("/status/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void changeUserAccountStatus(
            @PathVariable Integer userId, @RequestBody StatusRequest request
            )  {
       service.changeUserAccountStatus(userId,request);
    }

}
