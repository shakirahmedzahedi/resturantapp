package com.example.restaurant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class BusinessDateService {

    private final ZoneId zoneId;

    public BusinessDateService(
            @Value("${restaurant.business-zone:Europe/Stockholm}") String businessZone
    ) {
        this.zoneId = ZoneId.of(businessZone);
    }

    public LocalDate today() {
        return LocalDate.now(zoneId);
    }

    public ZoneId zoneId() {
        return zoneId;
    }
}
