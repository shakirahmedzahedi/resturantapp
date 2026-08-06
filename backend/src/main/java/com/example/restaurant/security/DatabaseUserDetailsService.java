package com.example.restaurant.security;

import com.example.restaurant.domain.AppUser;
import com.example.restaurant.repository.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;

    public DatabaseUserDetailsService(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser user = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .disabled(!user.isActive())
                .build();
    }
}
