package com.example.restaurant.controller;

import com.example.restaurant.dto.*;
import com.example.restaurant.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Counter product list and admin product management")
@SecurityRequirement(name = "basicAuth")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List active products",
               description = "Used by order-taking tablets. Inactive products are excluded.")
    public List<ProductResponse> listActive() {
        return service.activeProducts();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all products",
               description = "Admin-only list including active and inactive products.")
    public List<ProductResponse> listAll() {
        return service.allProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a product")
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{productId}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate or deactivate a product",
               description = "Inactive products remain in old orders but are hidden from counters.")
    public ProductResponse setActive(
            @PathVariable Long productId,
            @Valid @RequestBody SetProductActiveRequest request) {
        return service.setActive(productId, request);
    }
}
