package com.bookstore.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//For user to see
public class OrderItemDTO {
    private Long bookId;
    private int quantity;
    private double totalCost;
}
