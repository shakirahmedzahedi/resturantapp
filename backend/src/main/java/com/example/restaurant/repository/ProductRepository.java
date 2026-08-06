package com.example.restaurant.repository;

import com.example.restaurant.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrueOrderByDisplayOrderAsc();
    List<Product> findAllByOrderByDisplayOrderAsc();
    boolean existsByProductCodeIgnoreCase(String productCode);
}
