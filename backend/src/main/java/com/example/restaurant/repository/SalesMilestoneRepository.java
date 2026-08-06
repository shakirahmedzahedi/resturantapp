package com.example.restaurant.repository;
import com.example.restaurant.domain.SalesMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public interface SalesMilestoneRepository extends JpaRepository<SalesMilestone,Long>{
 boolean existsByBusinessDateAndThresholdAmount(LocalDate businessDate, BigDecimal thresholdAmount);
 List<SalesMilestone> findByBusinessDateAndIdGreaterThanOrderByIdAsc(LocalDate businessDate, Long afterId);
}
