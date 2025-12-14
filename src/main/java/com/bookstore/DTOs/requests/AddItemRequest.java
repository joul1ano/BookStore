package com.bookstore.DTOs.requests;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {
    @Positive(message = "Book id must be a positive number")
    private Long bookId;
    @PositiveOrZero(message = "Quantity must be a non-negative number")
    private int quantity;
}
