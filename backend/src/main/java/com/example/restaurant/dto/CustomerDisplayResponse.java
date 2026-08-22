package com.example.restaurant.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Customer display for one business day.
 * received = all orders still waiting (NEW)
 * ready    = latest completed orders, capped by the service (currently 10)
 */
public record CustomerDisplayResponse(
        LocalDate businessDate,
        List<CustomerDisplayOrderResponse> received,
        List<CustomerDisplayOrderResponse> ready
) {
}
