package com.example.restaurant.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/health/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer-display")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products")
                            .authenticated()
                        .requestMatchers("/api/products/admin")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/active")
                            .hasRole("ADMIN")
                        .requestMatchers("/api/admin/dashboard/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/notifications").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders/**")
                            .hasAnyRole("ORDER_TAKER", "KITCHEN", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders")
                            .hasAnyRole("ORDER_TAKER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status")
                            .hasAnyRole("KITCHEN", "ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Invalid username or password\"}");
                }))
                .build();
    }
}
