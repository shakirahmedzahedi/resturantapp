package com.example.restaurant.service;
import com.example.restaurant.domain.*;
import com.example.restaurant.dto.*;
import com.example.restaurant.event.OrderEvent;
import com.example.restaurant.exception.*;
import com.example.restaurant.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@Service public class OrderService{
 private final RestaurantOrderRepository orders;private final ProductRepository products;private final OrderPositionRepository positions;private final AppUserRepository users;private final TokenService tokenService;private final ApplicationEventPublisher events;private final SalesMilestoneService milestones;
 public OrderService(RestaurantOrderRepository orders,ProductRepository products,OrderPositionRepository positions,AppUserRepository users,TokenService tokenService,ApplicationEventPublisher events,SalesMilestoneService milestones){this.orders=orders;this.products=products;this.positions=positions;this.users=users;this.tokenService=tokenService;this.events=events;this.milestones=milestones;}
 @Transactional public OrderResponse create(CreateOrderRequest request,Authentication auth){AppUser user=users.findByUsername(auth.getName()).orElseThrow(()->new NotFoundException("Logged-in user not found"));OrderPosition position=positions.findById(request.positionId()).filter(OrderPosition::isActive).orElseThrow(()->new NotFoundException("Order position not found"));LocalDate today=LocalDate.now();RestaurantOrder order=new RestaurantOrder(tokenService.nextToken(today),today,position,user,request.paymentMethod());BigDecimal total=BigDecimal.ZERO;Set<Long> seen=new HashSet<>();for(CreateOrderRequest.Item r:request.items()){if(!seen.add(r.productId()))throw new BadRequestException("The same product appears more than once");Product p=products.findById(r.productId()).filter(Product::isActive).orElseThrow(()->new NotFoundException("Product not found or inactive: "+r.productId()));OrderItem item=new OrderItem(p,r.quantity());order.addItem(item);total=total.add(item.getLineTotal());}order.setTotalAmount(total);RestaurantOrder saved=orders.saveAndFlush(order);if(saved.getPaymentMethod()==PaymentMethod.SWISH)milestones.checkSwishMilestones(today);events.publishEvent(OrderEvent.created(saved.getId(),saved.getTokenNumber(),position.getPositionCode(),user.getUsername()));return OrderResponse.from(saved);}
 @Transactional(readOnly=true) public List<OrderResponse> today(OrderStatus status){List<RestaurantOrder> list=status==null?orders.findByBusinessDateOrderByCreatedAtDesc(LocalDate.now()):orders.findByBusinessDateAndStatusOrderByCreatedAtAsc(LocalDate.now(),status);return list.stream().map(OrderResponse::from).toList();}
 @Transactional(readOnly=true) public OrderResponse get(Long id){return OrderResponse.from(find(id));}
 @Transactional public OrderResponse updateStatus(Long id,UpdateStatusRequest request,Authentication auth){RestaurantOrder o=find(id);OrderStatus old=o.getStatus(),next=request.status();if(old!=OrderStatus.NEW)throw new BadRequestException("Only NEW orders can be completed or cancelled");if(next!=OrderStatus.COMPLETED&&next!=OrderStatus.CANCELLED)throw new BadRequestException("A NEW order can only change to COMPLETED or CANCELLED");o.setStatus(next);events.publishEvent(OrderEvent.statusChanged(o.getId(),o.getTokenNumber(),o.getPosition().getPositionCode(),old.name(),next.name(),auth.getName()));return OrderResponse.from(o);}
 private RestaurantOrder find(Long id){return orders.findDetailedById(id).orElseThrow(()->new NotFoundException("Order not found: "+id));}
}
