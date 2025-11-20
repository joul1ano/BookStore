package com.bookstore.model;

import com.bookstore.enums.OrderStatus;
import com.bookstore.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Join;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

//    @Column(name = "user_id")
//    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_placed")
    private LocalDate datePlaced;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "total_cost")
    private double totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "user_notes")
    private String userNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "status_last_updated")
    private LocalDate statusLastUpdated;


}
