package com.vietquan.security.service;

import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.Role;
import com.vietquan.security.exception.InvalidPasswordException;
import com.vietquan.security.exception.MisMatchPasswordException;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.request.ChangeInformationRequest;
import com.vietquan.security.request.ChangePasswordRequest;
import com.vietquan.security.request.StatusRequest;
import com.vietquan.security.response.UserInfoResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private static final int MAX_AVATAR_LENGTH = 1048576;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) throws InvalidPasswordException, MisMatchPasswordException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        //check the current password is correct

        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new MisMatchPasswordException("password does not match");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        repository.save(user);

    }

    public UserInfoResponse changeInfo(ChangeInformationRequest request, Integer userId) {
        Optional<User> userOptional = repository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("No user was found");
        }

        User user = userOptional.get();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhoneNumber(request.getPhone());

        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            try {
                byte[] avatarBytes = request.getAvatarFile().getBytes();
                if (avatarBytes.length > MAX_AVATAR_LENGTH) {
                    throw new IllegalArgumentException("Avatar size exceeds the maximum allowed size");
                }
                user.setAvatar(avatarBytes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process avatar file", e);
            }
        }


        repository.save(user);

        return UserInfoResponse.builder().email(user.getEmail()).img(user.getAvatar()).phone(user.getPhoneNumber()).firstname(user.getFirstname()).lastname(user.getLastname()).build();
    }


    public UserInfoResponse getUserInfo(Integer userId) {
        Optional<User> user = repository.findById(userId);
        UserInfoResponse response = new UserInfoResponse();
        response.setFirstname(user.get().getFirstname());
        response.setLastname(user.get().getLastname());
        response.setEmail(user.get().getEmail());
        response.setPhone(user.get().getPhoneNumber());
        response.setImg(user.get().getAvatar());
        return response;

    }

    public Page<UserInfoResponse> getAllUserInfo(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<User> users = repository.findAllByRole(Role.USER,pageable);
        List<UserInfoResponse> responses = new ArrayList<>();
        for (User user : users) {
            UserInfoResponse response = new UserInfoResponse();
            response.setUserId(user.getId());
            response.setFirstname(user.getFirstname());
            response.setLastname(user.getLastname());
            response.setPhone(user.getPhoneNumber());
            response.setEmail(user.getEmail());
            response.setImg(user.getAvatar());
            response.setStatus(user.isBanned());
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, users.getTotalElements());
    }
    public void changeUserAccountStatus(Integer userId, StatusRequest request){
        Optional<User> user=repository.findById(userId);
        if(user.isPresent()){

            user.get().setBanned(request.isRequest());
            repository.save(user.get());
        }
        else{
            throw new EntityNotFoundException("not found user");
        }


    }
}
