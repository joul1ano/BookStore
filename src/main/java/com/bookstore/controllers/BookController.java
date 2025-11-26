package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
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
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO){
        //BookDTO createdBookDTO = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}
