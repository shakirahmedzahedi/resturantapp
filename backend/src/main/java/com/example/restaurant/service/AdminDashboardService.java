package com.example.restaurant.service;

import com.example.restaurant.domain.OrderStatus;
import com.example.restaurant.domain.PaymentMethod;
import com.example.restaurant.domain.RestaurantOrder;
import com.example.restaurant.dto.AdminDashboardResponse;
import com.example.restaurant.dto.Counter4DashboardResponse;
import com.example.restaurant.dto.ItemSalesResponse;
import com.example.restaurant.dto.OrderResponse;
import com.example.restaurant.repository.RestaurantOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminDashboardService {

    private static final String COUNTER_4 = "COUNTER-4";

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

    /**
     * Quantity sold for every product on a business date.
     * Cancelled orders are not included. Counter 4 is included because this is an admin report.
     */
    @Transactional(readOnly = true)
    public List<ItemSalesResponse> itemsSold(LocalDate date) {
        return orders.findItemsSoldByBusinessDate(date)
                .stream()
                .map(row -> new ItemSalesResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Number) row[4]).longValue()
                ))
                .toList();
    }

    /**
     * Admin-only Counter 4 view. The live board contains NEW orders only.
     * Completed/cancelled orders remain included in the summary/report counts.
     */
    @Transactional(readOnly = true)
    public Counter4DashboardResponse counter4(LocalDate date) {
        List<RestaurantOrder> counter4Orders =
                orders.findByBusinessDateAndPosition_PositionCodeOrderByCreatedAtDesc(
                        date,
                        COUNTER_4
                );

        List<OrderResponse> pendingOrders = counter4Orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .map(OrderResponse::from)
                .toList();

        return new Counter4DashboardResponse(
                date,
                counter4Orders.size(),
                count(counter4Orders, OrderStatus.NEW),
                count(counter4Orders, OrderStatus.COMPLETED),
                count(counter4Orders, OrderStatus.CANCELLED),
                orders.sumNonCancelledOrderValueByPositionCode(date, COUNTER_4),
                orders.sumNonCancelledOrderValueByPositionCodeAndPaymentMethod(
                        date, COUNTER_4, PaymentMethod.SWISH
                ),
                orders.sumNonCancelledOrderValueByPositionCodeAndPaymentMethod(
                        date, COUNTER_4, PaymentMethod.CASH
                ),
                pendingOrders
        );
    }

    private long count(List<RestaurantOrder> list, OrderStatus status) {
        return list.stream()
                .filter(order -> order.getStatus() == status)
                .count();
    }
}
