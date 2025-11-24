package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
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
    public BookDTO getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO bookDTO){
        return bookService.createBook(bookDTO);
    }

}
