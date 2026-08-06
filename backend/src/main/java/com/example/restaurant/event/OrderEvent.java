package com.example.restaurant.event;

import java.time.Instant;
import java.util.UUID;

public record OrderEvent(
        UUID eventId,
        String eventType,
        Long orderId,
        int tokenNumber,
        String positionCode,
        String previousStatus,
        String newStatus,
        String changedBy,
        Instant occurredAt
) {
    public static OrderEvent created(Long orderId, int token, String position, String user) {
        return new OrderEvent(UUID.randomUUID(), "ORDER_CREATED", orderId, token,
                position, null, "NEW", user, Instant.now());
    }

    public static OrderEvent statusChanged(Long orderId, int token, String position,
                                           String oldStatus, String newStatus, String user) {
        return new OrderEvent(UUID.randomUUID(), "ORDER_STATUS_CHANGED", orderId, token,
                position, oldStatus, newStatus, user, Instant.now());
    }
}
