package com.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "shopping_carts")
@Entity
@ToString(exclude = {"user", "shoppingCartItems"})
public class ShoppingCart {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "item_count")
    private int itemCount;

    @Column(name = "total_cost")
    private double totalCost;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @OneToMany(mappedBy = "shoppingCart", fetch = FetchType.LAZY)
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
}
