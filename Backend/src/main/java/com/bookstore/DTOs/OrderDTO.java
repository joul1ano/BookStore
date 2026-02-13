package com.bookstore.DTOs;

import com.bookstore.enums.OrderStatus;
import com.bookstore.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime datePlaced;
    private String shippingAddress;
    private double totalCost;
    private PaymentMethod paymentMethod;
    private String userNotes;
    private OrderStatus status;
    private LocalDateTime statusLastUpdated;

}
