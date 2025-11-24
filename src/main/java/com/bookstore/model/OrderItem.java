//package com.bookstore.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "order_items")
//@Entity
//public class OrderItem {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)// MHPWS FETCH TYPE EAGER?
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    @Column(name = "book_id")
//    private Long bookId;
//
//    @Column(name = "quantity")
//    private int quantity;
//
//    @Column(name = "total_cost")
//    private double totalCost;
//}
