package com.bookstore.DTOs.requests;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFavouriteRequest {
    @Positive(message = "Book id must be a positive number")
    private Long bookId;
}
