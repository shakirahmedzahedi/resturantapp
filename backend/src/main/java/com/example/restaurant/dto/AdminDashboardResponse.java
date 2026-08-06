package com.example.restaurant.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public record AdminDashboardResponse(LocalDate businessDate,long totalOrders,long newOrders,long completedOrders,long cancelledOrders,BigDecimal totalSales,BigDecimal swishSales,BigDecimal cashSales,List<OrderResponse> orders){}
