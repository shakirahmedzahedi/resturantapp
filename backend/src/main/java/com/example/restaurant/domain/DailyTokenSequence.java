package com.example.restaurant.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_token_sequence")
public class DailyTokenSequence {
    @Id
    @Column(name = "business_date")
    private LocalDate businessDate;

    @Column(name = "last_token", nullable = false)
    private int lastToken;

    protected DailyTokenSequence() {}

    public DailyTokenSequence(LocalDate businessDate, int lastToken) {
        this.businessDate = businessDate;
        this.lastToken = lastToken;
    }

    public int next() {
        lastToken++;
        return lastToken;
    }

    public LocalDate getBusinessDate() { return businessDate; }
    public int getLastToken() { return lastToken; }
}
