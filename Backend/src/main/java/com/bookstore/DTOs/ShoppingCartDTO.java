package com.bookstore.DTOs;

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
public class ShoppingCartDTO {
    private Long userId;
    private Integer itemCount;
    private Double totalCost;
    private LocalDateTime lastUpdatedAt;

}
