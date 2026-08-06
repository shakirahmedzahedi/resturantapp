package com.example.restaurant.dto;
import com.example.restaurant.domain.RestaurantOrder;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
public record OrderResponse(Long id,int tokenNumber,LocalDate businessDate,String positionCode,String positionName,String createdBy,String status,String paymentMethod,BigDecimal totalAmount,LocalDateTime createdAt,List<Item> items){
 public record Item(Long productId,String productCode,String nameEn,String nameBn,int quantity,BigDecimal unitPrice,BigDecimal lineTotal){}
 public static OrderResponse from(RestaurantOrder o){return new OrderResponse(o.getId(),o.getTokenNumber(),o.getBusinessDate(),o.getPosition().getPositionCode(),o.getPosition().getPositionName(),o.getCreatedBy().getUsername(),o.getStatus().name(),o.getPaymentMethod().name(),o.getTotalAmount(),o.getCreatedAt(),o.getItems().stream().map(i->new Item(i.getProduct().getId(),i.getProduct().getProductCode(),i.getProduct().getNameEn(),i.getProduct().getNameBn(),i.getQuantity(),i.getUnitPrice(),i.getLineTotal())).toList());}
}
