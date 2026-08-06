package com.example.restaurant.config;

import com.example.restaurant.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordResetConfig {

    @Bean
    CommandLineRunner resetInitialPasswords(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String encodedPassword =
                    passwordEncoder.encode("ChangeMe123!");

            userRepository.findAll().forEach(user -> {
                user.setPasswordHash(encodedPassword);
                userRepository.save(user);
            });
        };
    }
}
