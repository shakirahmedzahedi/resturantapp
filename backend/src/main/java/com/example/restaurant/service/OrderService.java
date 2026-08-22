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

    @Transactional
    public OrderResponse create(CreateOrderRequest request, Authentication auth) {
        AppUser user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        OrderPosition position = positions.findById(request.positionId())
                .filter(OrderPosition::isActive)
                .orElseThrow(() -> new NotFoundException("Order position not found"));

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
                throw new BadRequestException("The same product appears more than once");
            }

            Product product = products.findById(requestItem.productId())
                    .filter(Product::isActive)
                    .orElseThrow(() -> new NotFoundException(
                            "Product not found or inactive: " + requestItem.productId()
                    ));

            OrderItem item = new OrderItem(product, requestItem.quantity());
            order.addItem(item);
            total = total.add(item.getLineTotal());
        }

        order.setTotalAmount(total);
        RestaurantOrder saved = orders.saveAndFlush(order);

        if (saved.getPaymentMethod() == PaymentMethod.SWISH) {
            milestones.checkSwishMilestones(today);
        }

        events.publishEvent(OrderEvent.created(
                saved.getId(),
                saved.getTokenNumber(),
                position.getPositionCode(),
                user.getUsername()
        ));

        return OrderResponse.from(saved);
    }

    /**
     * Counter/kitchen view: intentionally returns today only.
     * A new business date therefore starts with an empty frontend view after midnight,
     * while historical rows remain safely stored in the database for Admin reporting.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> today(OrderStatus status) {
        LocalDate today = businessDateService.today();

        List<RestaurantOrder> list = status == null
                ? orders.findByBusinessDateOrderByCreatedAtDesc(today)
                : orders.findByBusinessDateAndStatusOrderByCreatedAtAsc(today, status);

        return list.stream()
                .map(OrderResponse::from)
                .toList();
    }

    /**
     * Public customer screen.
     * received = every NEW order for today (oldest first)
     * ready    = only the 10 most recently COMPLETED orders (newest first)
     */
    @Transactional(readOnly = true)
    public CustomerDisplayResponse customerDisplayToday() {
        LocalDate today = businessDateService.today();

        List<CustomerDisplayOrderResponse> received = orders
                .findByBusinessDateAndStatusOrderByCreatedAtAsc(today, OrderStatus.NEW)
                .stream()
                .map(CustomerDisplayOrderResponse::from)
                .toList();

        List<CustomerDisplayOrderResponse> ready = orders
                .findTop10ByBusinessDateAndStatusOrderByUpdatedAtDesc(today, OrderStatus.COMPLETED)
                .stream()
                .map(CustomerDisplayOrderResponse::from)
                .toList();

        return new CustomerDisplayResponse(today, received, ready);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return OrderResponse.from(find(id));
    }

    @Transactional
    public OrderResponse updateStatus(
            Long id,
            UpdateStatusRequest request,
            Authentication auth
    ) {
        RestaurantOrder order = find(id);
        OrderStatus oldStatus = order.getStatus();
        OrderStatus nextStatus = request.status();

        if (oldStatus != OrderStatus.NEW) {
            throw new BadRequestException("Only NEW orders can be completed or cancelled");
        }

        if (nextStatus != OrderStatus.COMPLETED && nextStatus != OrderStatus.CANCELLED) {
            throw new BadRequestException(
                    "A NEW order can only change to COMPLETED or CANCELLED"
            );
        }

        order.setStatus(nextStatus);

        // Flush now so @PreUpdate updates updatedAt before the response/event is created.
        RestaurantOrder saved = orders.saveAndFlush(order);

        events.publishEvent(OrderEvent.statusChanged(
                saved.getId(),
                saved.getTokenNumber(),
                saved.getPosition().getPositionCode(),
                oldStatus.name(),
                nextStatus.name(),
                auth.getName()
        ));

        return OrderResponse.from(saved);
    }

    private RestaurantOrder find(Long id) {
        return orders.findDetailedById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }
}
