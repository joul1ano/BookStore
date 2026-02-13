package com.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "order_items")
@Entity
//@ToString(exclude = "order")
public class OrderItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)// MHPWS FETCH TYPE EAGER?
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_cost")
    private double totalCost;
}
