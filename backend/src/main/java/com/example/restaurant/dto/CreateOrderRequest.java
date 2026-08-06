package com.example.restaurant.dto;
import com.example.restaurant.domain.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
public record CreateOrderRequest(@NotNull Long positionId,@NotNull PaymentMethod paymentMethod,@NotEmpty List<@Valid Item> items){public record Item(@NotNull Long productId,@Positive int quantity){}}
