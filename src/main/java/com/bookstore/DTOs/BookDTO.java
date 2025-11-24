package com.bookstore.DTOs;

import com.bookstore.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String description;
    private Genre genre;
    private int numberOfPages;
    private double price;
    private int availability;
    private Long publisherId;
}
