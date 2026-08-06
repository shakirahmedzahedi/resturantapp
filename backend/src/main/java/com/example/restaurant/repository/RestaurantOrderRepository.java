package com.example.restaurant.repository;
import com.example.restaurant.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder,Long>{
 @EntityGraph(attributePaths={"items","items.product","position","createdBy"}) Optional<RestaurantOrder> findDetailedById(Long id);
 @EntityGraph(attributePaths={"items","items.product","position","createdBy"}) List<RestaurantOrder> findByBusinessDateOrderByCreatedAtDesc(LocalDate date);
 @EntityGraph(attributePaths={"items","items.product","position","createdBy"}) List<RestaurantOrder> findByBusinessDateAndStatusOrderByCreatedAtAsc(LocalDate date,OrderStatus status);
 @Query("select coalesce(sum(o.totalAmount),0) from RestaurantOrder o where o.businessDate=:date and o.status<>com.example.restaurant.domain.OrderStatus.CANCELLED") BigDecimal sumNonCancelledOrderValue(@Param("date") LocalDate date);
 @Query("select coalesce(sum(o.totalAmount),0) from RestaurantOrder o where o.businessDate=:date and o.paymentMethod=:method and o.status<>com.example.restaurant.domain.OrderStatus.CANCELLED") BigDecimal sumNonCancelledOrderValueByPaymentMethod(@Param("date") LocalDate date,@Param("method") PaymentMethod method);
}
