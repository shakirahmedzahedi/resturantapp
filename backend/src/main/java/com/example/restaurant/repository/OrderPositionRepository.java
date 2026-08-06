package com.example.restaurant.repository;

import com.example.restaurant.domain.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {}
