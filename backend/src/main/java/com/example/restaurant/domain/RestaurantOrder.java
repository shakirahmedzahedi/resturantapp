package com.example.restaurant.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="restaurant_orders", uniqueConstraints=@UniqueConstraint(name="uk_daily_token", columnNames={"business_date","token_number"}))
public class RestaurantOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="token_number",nullable=false) private int tokenNumber;
 @Column(name="business_date",nullable=false) private LocalDate businessDate;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="position_id",nullable=false) private OrderPosition position;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="created_by",nullable=false) private AppUser createdBy;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private OrderStatus status=OrderStatus.NEW;
 @Enumerated(EnumType.STRING) @Column(name="payment_method",nullable=false,length=20) private PaymentMethod paymentMethod;
 @Column(name="total_amount",nullable=false,precision=10,scale=2) private BigDecimal totalAmount=BigDecimal.ZERO;
 @Version @Column(name="version_number",nullable=false) private long versionNumber;
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
 @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) @OrderBy("id ASC") private List<OrderItem> items=new ArrayList<>();
 protected RestaurantOrder(){}
 public RestaurantOrder(int tokenNumber,LocalDate businessDate,OrderPosition position,AppUser createdBy,PaymentMethod paymentMethod){this.tokenNumber=tokenNumber;this.businessDate=businessDate;this.position=position;this.createdBy=createdBy;this.paymentMethod=paymentMethod;}
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();updatedAt=createdAt;}
 @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
 public void addItem(OrderItem item){items.add(item);item.setOrder(this);} public void setTotalAmount(BigDecimal v){totalAmount=v;} public void setStatus(OrderStatus v){status=v;}
 public Long getId(){return id;} public int getTokenNumber(){return tokenNumber;} public LocalDate getBusinessDate(){return businessDate;} public OrderPosition getPosition(){return position;} public AppUser getCreatedBy(){return createdBy;} public OrderStatus getStatus(){return status;} public PaymentMethod getPaymentMethod(){return paymentMethod;} public BigDecimal getTotalAmount(){return totalAmount;} public long getVersionNumber(){return versionNumber;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;} public List<OrderItem> getItems(){return items;}
}
