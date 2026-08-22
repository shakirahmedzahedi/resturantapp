package com.example.restaurant.service;
import com.example.restaurant.domain.*;
import com.example.restaurant.dto.SalesNotificationResponse;
import com.example.restaurant.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.List;
@Service
public class SalesMilestoneService{
 private static final BigDecimal STEP=new BigDecimal("5000.00"); private final RestaurantOrderRepository orders; private final SalesMilestoneRepository milestones;
 public SalesMilestoneService(RestaurantOrderRepository orders,SalesMilestoneRepository milestones){this.orders=orders;this.milestones=milestones;}
 @Transactional public void checkSwishMilestones(LocalDate date){BigDecimal total=orders.sumNonCancelledOrderValueByPaymentMethod(date,PaymentMethod.SWISH);long reached=total.divide(STEP,0,RoundingMode.FLOOR).longValue();for(long i=1;i<=reached;i++){BigDecimal threshold=STEP.multiply(BigDecimal.valueOf(i));if(milestones.existsByBusinessDateAndThresholdAmount(date,threshold))continue;try{milestones.saveAndFlush(new SalesMilestone(date,threshold,total));}catch(DataIntegrityViolationException ignored){}}}
 @Transactional(readOnly=true) public List<SalesNotificationResponse> notificationsAfter(LocalDate date,long afterId){return milestones.findByBusinessDateAndIdGreaterThanOrderByIdAsc(date,afterId).stream().map(SalesNotificationResponse::from).toList();}
}
