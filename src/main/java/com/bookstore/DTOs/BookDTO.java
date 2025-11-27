package com.bookstore.DTOs;

import com.bookstore.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    private String author;
    private String description;
    private Genre genre;
    @Positive(message = "Number of pages must be a positive number")
    private Integer numberOfPages;
    @Positive(message = "Price must be a positive number")
    private Double price;
    @PositiveOrZero(message = "Availability cannot be negative")
    private Integer availability;
    private Long publisherId;
}
