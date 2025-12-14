package com.bookstore.DTOs.requests;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemRequest {
    @PositiveOrZero(message = "Quantity must be a non negative number")
    private Integer quantity;
}
