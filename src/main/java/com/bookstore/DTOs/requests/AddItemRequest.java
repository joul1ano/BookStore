package com.bookstore.DTOs.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {
    private Long bookId;
    private int quantity;
}
