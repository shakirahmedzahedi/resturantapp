package com.example.restaurant.service;

import com.example.restaurant.domain.OrderStatus;
import com.example.restaurant.domain.PaymentMethod;
import com.example.restaurant.domain.RestaurantOrder;
import com.example.restaurant.dto.AdminDashboardResponse;
import com.example.restaurant.dto.OrderResponse;
import com.example.restaurant.repository.RestaurantOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminDashboardService {

    private final RestaurantOrderRepository orders;

    public AdminDashboardService(RestaurantOrderRepository orders) {
        this.orders = orders;
    }

    /**
     * Full report for exactly one business date.
     * The frontend can change the date without changing or deleting any stored order data.
     */
    @Transactional(readOnly = true)
    public AdminDashboardResponse dashboard(LocalDate date) {
        List<RestaurantOrder> all = orders.findByBusinessDateOrderByCreatedAtDesc(date);

        return new AdminDashboardResponse(
                date,
                all.size(),
                count(all, OrderStatus.NEW),
                count(all, OrderStatus.COMPLETED),
                count(all, OrderStatus.CANCELLED),
                orders.sumNonCancelledOrderValue(date),
                orders.sumNonCancelledOrderValueByPaymentMethod(date, PaymentMethod.SWISH),
                orders.sumNonCancelledOrderValueByPaymentMethod(date, PaymentMethod.CASH),
                all.stream().map(OrderResponse::from).toList()
        );
    }

    private long count(List<RestaurantOrder> list, OrderStatus status) {
        return list.stream()
                .filter(order -> order.getStatus() == status)
                .count();
    }
}
