package com.bookstore.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItemDTO {
    private Long shoppingCartId;
    private BookDTO bookDTO;
    private Integer quantity;
}
