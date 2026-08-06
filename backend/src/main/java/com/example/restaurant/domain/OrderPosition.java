package com.example.restaurant.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_positions")
public class OrderPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_code", nullable = false, unique = true, length = 30)
    private String positionCode;

    @Column(name = "position_name", nullable = false, length = 100)
    private String positionName;

    @Column(nullable = false)
    private boolean active = true;

    protected OrderPosition() {}

    public Long getId() { return id; }
    public String getPositionCode() { return positionCode; }
    public String getPositionName() { return positionName; }
    public boolean isActive() { return active; }
}
