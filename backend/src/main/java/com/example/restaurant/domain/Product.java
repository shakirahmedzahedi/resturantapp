package com.example.restaurant.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true, length = 30)
    private String productCode;

    @Column(name = "name_en", length = 150)
    private String nameEn;

    @Column(name = "name_bn", nullable = false, length = 150)
    private String nameBn;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean active = true;

    protected Product() {}

    public Product(String productCode, String nameEn, String nameBn,
                   BigDecimal price, int displayOrder) {
        this.productCode = productCode;
        this.nameEn = nameEn;
        this.nameBn = nameBn;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.displayOrder = displayOrder;
        this.active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() { return id; }
    public String getProductCode() { return productCode; }
    public String getNameEn() { return nameEn; }
    public String getNameBn() { return nameBn; }
    public BigDecimal getPrice() { return price; }
    public int getDisplayOrder() { return displayOrder; }
    public boolean isActive() { return active; }
}
