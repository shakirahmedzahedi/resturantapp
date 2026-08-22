package com.example.restaurant.service;

import com.example.restaurant.domain.AppUser;
import com.example.restaurant.domain.OrderItem;
import com.example.restaurant.domain.OrderPosition;
import com.example.restaurant.domain.OrderStatus;
import com.example.restaurant.domain.PaymentMethod;
import com.example.restaurant.domain.Product;
import com.example.restaurant.domain.RestaurantOrder;
import com.example.restaurant.dto.CreateOrderRequest;
import com.example.restaurant.dto.CustomerDisplayOrderResponse;
import com.example.restaurant.dto.CustomerDisplayResponse;
import com.example.restaurant.dto.OrderResponse;
import com.example.restaurant.dto.UpdateStatusRequest;
import com.example.restaurant.event.OrderEvent;
import com.example.restaurant.exception.BadRequestException;
import com.example.restaurant.exception.NotFoundException;
import com.example.restaurant.repository.AppUserRepository;
import com.example.restaurant.repository.OrderPositionRepository;
import com.example.restaurant.repository.ProductRepository;
import com.example.restaurant.repository.RestaurantOrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    /*
     * Orders created from this counter are stored normally,
     * but are hidden from Kitchen and Customer Display.
     */
    private static final String EXCLUDED_COUNTER = "COUNTER-4";

    private final RestaurantOrderRepository orders;
    private final ProductRepository products;
    private final OrderPositionRepository positions;
    private final AppUserRepository users;
    private final TokenService tokenService;
    private final ApplicationEventPublisher events;
    private final SalesMilestoneService milestones;
    private final BusinessDateService businessDateService;

    public OrderService(
            RestaurantOrderRepository orders,
            ProductRepository products,
            OrderPositionRepository positions,
            AppUserRepository users,
            TokenService tokenService,
            ApplicationEventPublisher events,
            SalesMilestoneService milestones,
            BusinessDateService businessDateService
    ) {
        this.orders = orders;
        this.products = products;
        this.positions = positions;
        this.users = users;
        this.tokenService = tokenService;
        this.events = events;
        this.milestones = milestones;
        this.businessDateService = businessDateService;
    }


    /*
     * Create order.
     *
     * Counter 4 behaves exactly like every other counter here.
     * Its order is stored in MySQL normally.
     */
    @Transactional
    public OrderResponse create(
            CreateOrderRequest request,
            Authentication auth
    ) {

        AppUser user = users.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Logged-in user not found"
                        )
                );

        OrderPosition position = positions.findById(request.positionId())
                .filter(OrderPosition::isActive)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Order position not found"
                        )
                );

        LocalDate today = businessDateService.today();

        RestaurantOrder order = new RestaurantOrder(
                tokenService.nextToken(today),
                today,
                position,
                user,
                request.paymentMethod()
        );

        BigDecimal total = BigDecimal.ZERO;

        Set<Long> seen = new HashSet<>();

        for (CreateOrderRequest.Item requestItem : request.items()) {

            if (!seen.add(requestItem.productId())) {
                throw new BadRequestException(
                        "The same product appears more than once"
                );
            }

            Product product = products
                    .findById(requestItem.productId())
                    .filter(Product::isActive)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Product not found or inactive: "
                                            + requestItem.productId()
                            )
                    );

            OrderItem item = new OrderItem(
                    product,
                    requestItem.quantity()
            );

            order.addItem(item);

            total = total.add(
                    item.getLineTotal()
            );
        }

        order.setTotalAmount(total);

        RestaurantOrder saved =
                orders.saveAndFlush(order);


        /*
         * Counter 4 is still included in SWISH milestone calculation.
         *
         * If you DON'T want Counter 4 Swish sales included,
         * this can also be changed separately.
         */
        if (saved.getPaymentMethod() == PaymentMethod.SWISH) {
            milestones.checkSwishMilestones(today);
        }


        events.publishEvent(
                OrderEvent.created(
                        saved.getId(),
                        saved.getTokenNumber(),
                        position.getPositionCode(),
                        user.getUsername()
                )
        );

        return OrderResponse.from(saved);
    }


    /*
     * Kitchen / normal order view.
     *
     * Counter 4 is excluded here.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> today(
            OrderStatus status
    ) {

        LocalDate today =
                businessDateService.today();

        List<RestaurantOrder> list;

        if (status == null) {

            list =
                    orders
                            .findByBusinessDateAndPosition_PositionCodeNotOrderByCreatedAtDesc(
                                    today,
                                    EXCLUDED_COUNTER
                            );

        } else {

            list =
                    orders
                            .findByBusinessDateAndStatusAndPosition_PositionCodeNotOrderByCreatedAtAsc(
                                    today,
                                    status,
                                    EXCLUDED_COUNTER
                            );
        }

        return list.stream()
                .map(OrderResponse::from)
                .toList();
    }


    /*
     * Public customer display.
     *
     * RECEIVED:
     * All NEW orders except Counter 4.
     *
     * READY:
     * Latest 20 COMPLETED orders except Counter 4.
     */
    @Transactional(readOnly = true)
    public CustomerDisplayResponse customerDisplayToday() {

        LocalDate today =
                businessDateService.today();


        /*
         * NEW / RECEIVED orders.
         *
         * Oldest first.
         */
        List<CustomerDisplayOrderResponse> received =
                orders
                        .findByBusinessDateAndStatusAndPosition_PositionCodeNotOrderByCreatedAtAsc(
                                today,
                                OrderStatus.NEW,
                                EXCLUDED_COUNTER
                        )
                        .stream()
                        .map(CustomerDisplayOrderResponse::from)
                        .toList();


        /*
         * READY orders.
         *
         * Latest 20 completed orders.
         * Counter 4 is excluded.
         */
        List<CustomerDisplayOrderResponse> ready =
                orders
                        .findTop20ByBusinessDateAndStatusAndPosition_PositionCodeNotOrderByUpdatedAtDesc(
                                today,
                                OrderStatus.COMPLETED,
                                EXCLUDED_COUNTER
                        )
                        .stream()
                        .map(CustomerDisplayOrderResponse::from)
                        .toList();


        return new CustomerDisplayResponse(
                today,
                received,
                ready
        );
    }


    /*
     * Get individual order.
     *
     * Admin or another authorised endpoint can still retrieve
     * Counter 4 orders by ID.
     */
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {

        return OrderResponse.from(
                find(id)
        );
    }


    /*
     * Update order status.
     *
     * Counter 4 can still be updated normally if an authorised
     * caller knows the order ID.
     */
    @Transactional
    public OrderResponse updateStatus(
            Long id,
            UpdateStatusRequest request,
            Authentication auth
    ) {

        RestaurantOrder order =
                find(id);

        OrderStatus oldStatus =
                order.getStatus();

        OrderStatus nextStatus =
                request.status();


        if (oldStatus != OrderStatus.NEW) {

            throw new BadRequestException(
                    "Only NEW orders can be completed or cancelled"
            );
        }


        if (
                nextStatus != OrderStatus.COMPLETED
                        &&
                        nextStatus != OrderStatus.CANCELLED
        ) {

            throw new BadRequestException(
                    "A NEW order can only change to COMPLETED or CANCELLED"
            );
        }


        order.setStatus(nextStatus);


        /*
         * Flush so @PreUpdate changes updatedAt before
         * response/event generation.
         */
        RestaurantOrder saved =
                orders.saveAndFlush(order);


        events.publishEvent(
                OrderEvent.statusChanged(
                        saved.getId(),
                        saved.getTokenNumber(),
                        saved.getPosition().getPositionCode(),
                        oldStatus.name(),
                        nextStatus.name(),
                        auth.getName()
                )
        );


        return OrderResponse.from(saved);
    }


    private RestaurantOrder find(Long id) {

        return orders
                .findDetailedById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Order not found: " + id
                        )
                );
    }
}