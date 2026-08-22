package com.example.restaurant.controller;

import com.example.restaurant.dto.CustomerDisplayResponse;
import com.example.restaurant.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-display")
@Tag(name = "Customer Display", description = "Public read-only order status for the customer screen")
public class CustomerDisplayController {

    private final OrderService orderService;

    public CustomerDisplayController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(
            summary = "Get today's customer order display",
            description = "Returns all NEW orders as received and the 10 most recently COMPLETED orders as ready."
    )
    public CustomerDisplayResponse today() {
        return orderService.customerDisplayToday();
    }
}
