package com.bookstore.mappers;

import com.bookstore.model.Book;
import com.bookstore.DTOs.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book){
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .genre(book.getGenre())
                .numberOfPages(book.getNumberOfPages())
                .price(book.getPrice())
                .availability(book.getAvailability())
                .publisherId(book.getPublisherId())
                .build();
    }

    public Book toEntity(BookDTO bookDTO){
        return Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .description(bookDTO.getDescription())
                .genre(bookDTO.getGenre())
                .numberOfPages(bookDTO.getNumberOfPages())
                .price(bookDTO.getPrice())
                .availability(bookDTO.getAvailability())
                .publisherId(bookDTO.getPublisherId())
                .build();
    }


}
