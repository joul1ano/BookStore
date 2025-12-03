package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@Positive(message = "Id must be a positive number") @PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO){
        //BookDTO createdBookDTO = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @Positive(message = "Id must be a positive number")
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            BookDTO bookDTO){
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@Positive(message = "Id must be a positive number")@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}
