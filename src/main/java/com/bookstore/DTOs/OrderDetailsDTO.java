package com.bookstore.DTOs;

import com.bookstore.enums.OrderStatus;
import com.bookstore.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDTO {
    private Long orderId;
    private Long userId;
    private LocalDateTime datePlaced;
    private String shippingAddress;
    private double totalCost;
    private PaymentMethod paymentMethod;
    private String userNotes;
    private OrderStatus status;
    private LocalDateTime statusLastUpdated;
    private List<UserOrderItemDTO> items;
}
