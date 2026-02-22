package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.PagedResponseDTO;
import com.bookstore.enums.Genre;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@Validated
@Tag(name = "Books", description = "Operations related to books")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a list of all books available. Access = [ADMIN,USER]")
    public PagedResponseDTO<BookDTO> getAllBooks(
            @RequestParam (required = false) Genre genre,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageable = PageRequest.of(page,size);
        return bookService.getAllBooks(auth, Optional.ofNullable(genre), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id",description = "Access = [ADMIN,USER]")
    public ResponseEntity<BookDTO> getBookById(@Positive(message = "Id must be a positive number") @PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new book", description = " Access = [ADMIN]")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a book by id", description = " Access = [ADMIN]")
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
    @Operation(summary = "Delete a book by id", description = " Access = [ADMIN]")
    public ResponseEntity<String> deleteBook(@Positive(message = "Id must be a positive number")@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}
