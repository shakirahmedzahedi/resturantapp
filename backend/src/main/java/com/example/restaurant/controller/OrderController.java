package com.example.restaurant.controller;

import com.example.restaurant.domain.OrderStatus;
import com.example.restaurant.dto.*;
import com.example.restaurant.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Create orders and manage kitchen order status")
@SecurityRequirement(name = "basicAuth")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create an order",
               description = "Available to order takers and administrators.")
    public OrderResponse create(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication authentication) {
        return service.create(request, authentication);
    }

    @GetMapping("/today")
    @Operation(summary = "List today's orders",
               description = "Optionally filter by order status.")
    public List<OrderResponse> today(
            @RequestParam(required = false) OrderStatus status) {
        return service.today(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one order")
    public OrderResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status",
               description = "Kitchen and admin only. The backend validates the status transition.")
    public OrderResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            Authentication authentication) {
        return service.updateStatus(id, request, authentication);
    }
}
