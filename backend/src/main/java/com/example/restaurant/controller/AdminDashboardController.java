package com.example.restaurant.controller;

import com.example.restaurant.dto.AdminDashboardResponse;
import com.example.restaurant.service.AdminDashboardService;
import com.example.restaurant.service.BusinessDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Admin Dashboard", description = "Date-wise sales and order history")
public class AdminDashboardController {

    private final AdminDashboardService service;
    private final BusinessDateService businessDateService;

    public AdminDashboardController(
            AdminDashboardService service,
            BusinessDateService businessDateService
    ) {
        this.service = service;
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
        return service.dashboard(date == null ? businessDateService.today() : date);
    }
}
