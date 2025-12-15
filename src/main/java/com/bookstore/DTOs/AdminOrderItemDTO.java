package com.bookstore.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//For admin to see
public class AdminOrderItemDTO {
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;
    private double totalCost;
}
