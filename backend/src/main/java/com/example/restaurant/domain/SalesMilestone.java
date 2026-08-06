package com.example.restaurant.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name="sales_milestones", uniqueConstraints=@UniqueConstraint(name="uk_sales_milestone", columnNames={"business_date","threshold_amount"}))
public class SalesMilestone {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="business_date",nullable=false) private LocalDate businessDate;
 @Column(name="threshold_amount",nullable=false,precision=12,scale=2) private BigDecimal thresholdAmount;
 @Column(name="total_sales",nullable=false,precision=12,scale=2) private BigDecimal totalSales;
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
 protected SalesMilestone() {}
 public SalesMilestone(LocalDate businessDate, BigDecimal thresholdAmount, BigDecimal totalSales){this.businessDate=businessDate;this.thresholdAmount=thresholdAmount;this.totalSales=totalSales;this.createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public LocalDate getBusinessDate(){return businessDate;} public BigDecimal getThresholdAmount(){return thresholdAmount;} public BigDecimal getTotalSales(){return totalSales;} public LocalDateTime getCreatedAt(){return createdAt;}
}
