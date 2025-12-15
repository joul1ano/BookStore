package com.bookstore.DTOs.requests;

import com.bookstore.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {
    private Long userId;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private String userNotes;
}
