package com.example.restaurant.service;

import com.example.restaurant.domain.Product;
import com.example.restaurant.dto.*;
import com.example.restaurant.exception.BadRequestException;
import com.example.restaurant.exception.NotFoundException;
import com.example.restaurant.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository products;

    public ProductService(ProductRepository products) {
        this.products = products;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> activeProducts() {
        return products.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> allProducts() {
        return products.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        String normalizedCode = request.productCode().trim().toUpperCase();

        if (products.existsByProductCodeIgnoreCase(normalizedCode)) {
            throw new BadRequestException(
                    "Product code already exists: " + normalizedCode);
        }

        String englishName = request.nameEn() == null
                ? null
                : request.nameEn().trim();

        Product product = new Product(
                normalizedCode,
                englishName,
                request.nameBn().trim(),
                request.price(),
                request.displayOrder());

        return ProductResponse.from(products.save(product));
    }

    @Transactional
    public ProductResponse setActive(Long productId, SetProductActiveRequest request) {
        Product product = products.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found: " + productId));

        product.setActive(request.active());
        return ProductResponse.from(product);
    }
}
