package com.example.restaurant.dto;

import jakarta.validation.constraints.NotNull;

public record SetProductActiveRequest(@NotNull Boolean active) {}
