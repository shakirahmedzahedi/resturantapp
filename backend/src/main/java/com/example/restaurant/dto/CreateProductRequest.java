package com.example.restaurant.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank
        @Size(max = 30)
        @Pattern(regexp = "^[A-Za-z0-9_-]+$",
                 message = "must contain only letters, numbers, underscore or hyphen")
        String productCode,

        @Size(max = 150)
        String nameEn,

        @NotBlank
        @Size(max = 150)
        String nameBn,

        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price,

        @Positive
        int displayOrder
) {}
