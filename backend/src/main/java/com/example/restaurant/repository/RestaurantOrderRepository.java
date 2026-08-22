package com.example.restaurant.repository;

import com.example.restaurant.domain.OrderStatus;
import com.example.restaurant.domain.PaymentMethod;
import com.example.restaurant.domain.RestaurantOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestaurantOrderRepository
        extends JpaRepository<RestaurantOrder, Long> {

    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    Optional<RestaurantOrder> findDetailedById(Long id);


    /*
     * Admin use.
     *
     * IMPORTANT:
     * Do NOT exclude Counter 4 here.
     * Admin should continue to see every order.
     */
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    List<RestaurantOrder> findByBusinessDateOrderByCreatedAtDesc(
            LocalDate date
    );


    /*
     * Existing unrestricted method.
     *
     * Keep it because other parts of the application may need
     * all orders, including Counter 4.
     */
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    List<RestaurantOrder> findByBusinessDateAndStatusOrderByCreatedAtAsc(
            LocalDate date,
            OrderStatus status
    );


    /*
     * Kitchen / normal order view.
     *
     * Excludes a specific counter.
     */
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    List<RestaurantOrder>
    findByBusinessDateAndPosition_PositionCodeNotOrderByCreatedAtDesc(
            LocalDate date,
            String excludedPositionCode
    );


    /*
     * Kitchen NEW orders.
     *
     * Excludes Counter 4 and keeps oldest order first.
     */
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    List<RestaurantOrder>
    findByBusinessDateAndStatusAndPosition_PositionCodeNotOrderByCreatedAtAsc(
            LocalDate date,
            OrderStatus status,
            String excludedPositionCode
    );


    /*
     * Customer display READY orders.
     *
     * Returns latest 20 completed orders,
     * excluding Counter 4.
     *
     * updatedAt represents the time when the order changed
     * from NEW to COMPLETED.
     */
    @EntityGraph(attributePaths = {
            "position"
    })
    List<RestaurantOrder>
    findTop20ByBusinessDateAndStatusAndPosition_PositionCodeNotAndUpdatedAtGreaterThanEqualOrderByUpdatedAtDesc(
            LocalDate date,
            OrderStatus status,
            String excludedPositionCode,
            LocalDateTime updatedAfter
    );


    /*
     * Total non-cancelled order value.
     *
     * This currently includes Counter 4.
     * Useful for Admin reporting.
     */
    @Query("""
            select coalesce(sum(o.totalAmount), 0)
            from RestaurantOrder o
            where o.businessDate = :date
              and o.status <> com.example.restaurant.domain.OrderStatus.CANCELLED
            """)
    BigDecimal sumNonCancelledOrderValue(
            @Param("date") LocalDate date
    );


    /*
     * Total non-cancelled value by payment method.
     *
     * This also includes Counter 4.
     *
     * Therefore Counter 4 SWISH orders still contribute
     * to the Swish sales milestone unless you decide
     * to exclude them separately.
     */
    @Query("""
            select coalesce(sum(o.totalAmount), 0)
            from RestaurantOrder o
            where o.businessDate = :date
              and o.paymentMethod = :method
              and o.status <> com.example.restaurant.domain.OrderStatus.CANCELLED
            """)
    BigDecimal sumNonCancelledOrderValueByPaymentMethod(
            @Param("date") LocalDate date,
            @Param("method") PaymentMethod method
    );


    /*
     * Admin - Counter 4 order list.
     */
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "position",
            "createdBy"
    })
    List<RestaurantOrder>
    findByBusinessDateAndPosition_PositionCodeOrderByCreatedAtDesc(
            LocalDate date,
            String positionCode
    );


    /*
     * Admin - quantities sold for each product on one business date.
     * Cancelled orders are excluded.
     */
    @Query("""
            select
                p.id,
                p.productCode,
                p.nameEn,
                p.nameBn,
                sum(i.quantity)
            from OrderItem i
            join i.order o
            join i.product p
            where o.businessDate = :date
              and o.status <> com.example.restaurant.domain.OrderStatus.CANCELLED
            group by p.id, p.productCode, p.nameEn, p.nameBn, p.displayOrder
            order by p.displayOrder asc
            """)
    List<Object[]> findItemsSoldByBusinessDate(
            @Param("date") LocalDate date
    );


    /*
     * Admin - total non-cancelled sales for one counter.
     */
    @Query("""
            select coalesce(sum(o.totalAmount), 0)
            from RestaurantOrder o
            where o.businessDate = :date
              and o.position.positionCode = :positionCode
              and o.status <> com.example.restaurant.domain.OrderStatus.CANCELLED
            """)
    BigDecimal sumNonCancelledOrderValueByPositionCode(
            @Param("date") LocalDate date,
            @Param("positionCode") String positionCode
    );


    /*
     * Admin - total non-cancelled sales for one counter and payment method.
     */
    @Query("""
            select coalesce(sum(o.totalAmount), 0)
            from RestaurantOrder o
            where o.businessDate = :date
              and o.position.positionCode = :positionCode
              and o.paymentMethod = :method
              and o.status <> com.example.restaurant.domain.OrderStatus.CANCELLED
            """)
    BigDecimal sumNonCancelledOrderValueByPositionCodeAndPaymentMethod(
            @Param("date") LocalDate date,
            @Param("positionCode") String positionCode,
            @Param("method") PaymentMethod method
    );
}