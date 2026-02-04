package com.bookstore.model;

import com.bookstore.enums.OrderStatus;
import com.bookstore.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Join;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "orders")
@Entity
//@ToString(exclude = {"user", "orderItems"})
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_placed")
    private LocalDateTime datePlaced;

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
    private LocalDateTime statusLastUpdated;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
