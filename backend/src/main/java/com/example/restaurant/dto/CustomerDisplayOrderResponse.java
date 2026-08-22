package com.example.restaurant.dto;

import com.example.restaurant.domain.RestaurantOrder;

import java.time.LocalDateTime;

/**
 * Minimal, public-safe order information for the customer-facing display.
 * No username, payment method, prices or order items are exposed.
 */
public record CustomerDisplayOrderResponse(
        Long id,
        int tokenNumber,
        String positionName,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CustomerDisplayOrderResponse from(RestaurantOrder order) {
        return new CustomerDisplayOrderResponse(
                order.getId(),
                order.getTokenNumber(),
                order.getPosition().getPositionName(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
