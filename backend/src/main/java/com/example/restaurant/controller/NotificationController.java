package com.example.restaurant.controller;

import com.example.restaurant.dto.SalesNotificationResponse;
import com.example.restaurant.service.BusinessDateService;
import com.example.restaurant.service.SalesMilestoneService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Notifications")
public class NotificationController {

    private final SalesMilestoneService service;
    private final BusinessDateService businessDateService;

    public NotificationController(
            SalesMilestoneService service,
            BusinessDateService businessDateService
    ) {
        this.service = service;
        this.businessDateService = businessDateService;
    }

    @GetMapping
    public List<SalesNotificationResponse> list(
            @RequestParam(defaultValue = "0") long afterId
    ) {
        return service.notificationsAfter(businessDateService.today(), afterId);
    }
}
