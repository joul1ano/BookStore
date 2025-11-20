package com.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "shopping_carts")
@Entity
public class ShoppingCart {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_count")
    private int itemCount;

    @Column(name = "total_cost")
    private double totalCost;

    @Column(name = "last_updated_at")
    private LocalDate lastUpdatedAt;
}
