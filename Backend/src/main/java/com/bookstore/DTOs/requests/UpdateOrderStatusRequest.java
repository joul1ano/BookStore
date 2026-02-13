package com.bookstore.DTOs.requests;

import com.bookstore.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
