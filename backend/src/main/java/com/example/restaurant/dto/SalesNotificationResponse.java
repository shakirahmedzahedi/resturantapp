package com.example.restaurant.dto;
import com.example.restaurant.domain.SalesMilestone;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record SalesNotificationResponse(Long id,BigDecimal thresholdAmount,BigDecimal totalSales,String message,LocalDateTime createdAt){public static SalesNotificationResponse from(SalesMilestone m){return new SalesNotificationResponse(m.getId(),m.getThresholdAmount(),m.getTotalSales(),"Swish orders have crossed "+m.getThresholdAmount().toPlainString()+" SEK!",m.getCreatedAt());}}
