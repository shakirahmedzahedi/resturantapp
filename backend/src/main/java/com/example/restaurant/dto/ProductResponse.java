package com.example.restaurant.dto;

import com.example.restaurant.domain.Product;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String code,
        String nameEn,
        String nameBn,
        BigDecimal price,
        int displayOrder,
        boolean active
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getProductCode(),
                p.getNameEn(),
                p.getNameBn(),
                p.getPrice(),
                p.getDisplayOrder(),
                p.isActive());
    }
}
