package com.example.restaurant.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI restaurantOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Token and Order API")
                        .version("1.0.0")
                        .description("""
                                API for three order-taking counters and one kitchen screen.
                                Counters create orders. Kitchen users can only view orders
                                and mark NEW orders as COMPLETED or CANCELLED. Admin users manage products.
                                """))
                .components(new Components()
                        .addSecuritySchemes(
                                "basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
