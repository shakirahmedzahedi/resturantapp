package com.example.restaurant.dto;

import com.example.restaurant.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull OrderStatus status) {}
