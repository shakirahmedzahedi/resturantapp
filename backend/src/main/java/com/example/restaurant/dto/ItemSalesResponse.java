package com.example.restaurant.dto;

public record ItemSalesResponse(
        Long productId,
        String productCode,
        String nameEn,
        String nameBn,
        long quantity
) {
}
