//package com.bookstore.model;
//
//import com.bookstore.enums.Role;
//import jakarta.persistence.*;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Table;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "users")
//@Entity
//public class User {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "username")
//    private String username;
//
//    @Column(name = "password")
//    private String password;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role")
//    private Role role;
//
//    @Column(name = "total_amount_spent")
//    private double totalAmountSpent;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "last_login_at")
//    private LocalDateTime lastLoginAt;
//
//    @OneToOne(mappedBy = "user")
//    private ShoppingCart shoppingCart;
//
//    @OneToMany(mappedBy = "user")
//    private List<Order> orders = new ArrayList<>();
//}
