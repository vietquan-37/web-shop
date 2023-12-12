package com.vietquan.security.creation;

import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.Role;
import com.vietquan.security.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor

public class AdminAccountCreator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static boolean adminAccountCreated = false;


    public void createAdminAccount() {
        if (adminAccountCreated) {
            return;
        }

        if (userRepository.findByEmail("bubakush20099@gmail.com").isPresent()) {
            adminAccountCreated = true;
            return;
        }

        User adminUser = User.builder()
                .email("bubakush20099@gmail.com")
                .password(passwordEncoder.encode("Quanbeo123"))
                .firstname("Viet")
                .avatar(null)
                .lastname("Quan")
                .role(Role.ADMIN)
                .build();

        userRepository.save(adminUser);

        adminAccountCreated = true;
    }
}
