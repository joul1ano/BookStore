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
//@Table(name = "shopping_cart_items")
//@Entity
//public class ShoppingCartItem {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shopping_cart_id")
//    private ShoppingCart shoppingCart;
//
//    @Column(name = "book_id")
//    private Long bookId;
//
//    @Column(name = "quantity")
//    private int quantity;
//}
