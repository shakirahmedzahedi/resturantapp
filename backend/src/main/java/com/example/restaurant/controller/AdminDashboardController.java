package com.example.restaurant.controller;

import com.example.restaurant.dto.AdminDashboardResponse;
import com.example.restaurant.dto.Counter4DashboardResponse;
import com.example.restaurant.dto.ItemSalesResponse;
import com.example.restaurant.dto.OrderResponse;
import com.example.restaurant.dto.UpdateStatusRequest;
import com.example.restaurant.service.AdminDashboardService;
import com.example.restaurant.service.BusinessDateService;
import com.example.restaurant.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Admin Dashboard", description = "Date-wise sales, item sales, Counter 4 management and order history")
public class AdminDashboardController {

    private final AdminDashboardService service;
    private final OrderService orderService;
    private final BusinessDateService businessDateService;

    public AdminDashboardController(
            AdminDashboardService service,
            OrderService orderService,
            BusinessDateService businessDateService
    ) {
        this.service = service;
        this.orderService = orderService;
        this.businessDateService = businessDateService;
    }

    @GetMapping
    @Operation(
            summary = "Get dashboard/report for one date",
            description = "Pass date=YYYY-MM-DD. If omitted, today's configured business date is used. Historical data is not deleted after midnight."
    )
    public AdminDashboardResponse dashboard(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return service.dashboard(resolveDate(date));
    }

    @GetMapping("/items-sold")
    @Operation(
            summary = "Get item quantities sold for one date",
            description = "Cancelled orders are excluded. Counter 4 is included in this admin-only report."
    )
    public List<ItemSalesResponse> itemsSold(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return service.itemsSold(resolveDate(date));
    }

    @GetMapping("/counter4")
    @Operation(
            summary = "Get Counter 4 dashboard",
            description = "Returns Counter 4 orders and sales totals for one date. Admin only."
    )
    public Counter4DashboardResponse counter4(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return service.counter4(resolveDate(date));
    }

    @PatchMapping("/counter4/orders/{id}/status")
    @Operation(
            summary = "Update a Counter 4 order status",
            description = "Admin only. Only Counter 4 NEW orders can become COMPLETED or CANCELLED."
    )
    public OrderResponse updateCounter4Status(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            Authentication authentication
    ) {
        return orderService.updateCounter4Status(id, request, authentication);
    }

    private LocalDate resolveDate(LocalDate date) {
        return date == null ? businessDateService.today() : date;
    }
}
