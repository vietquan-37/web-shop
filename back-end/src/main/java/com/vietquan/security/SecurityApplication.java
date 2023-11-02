package com.vietquan.security;

import com.vietquan.security.creation.AdminAccountCreator;
import com.vietquan.security.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SecurityApplication.class, args);
        AdminAccountCreator adminAccountCreator = context.getBean(AdminAccountCreator.class);
        adminAccountCreator.createAdminAccount();
    }

    @Bean
    public AdminAccountCreator adminAccountCreator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new AdminAccountCreator(userRepository, passwordEncoder);
    }
}
