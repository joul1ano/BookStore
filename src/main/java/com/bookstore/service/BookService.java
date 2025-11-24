package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BookService {
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public BookService (BookRepository bookRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getAllBooks()
    {
        return bookRepository.findAll().stream().map(bookMapper::toDTO).toList();
    }

    public BookDTO getBookById(Long id){
        return bookRepository.findById(id).map(bookMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

//    public BookDTO createBook(BookDTO bookDTO){
//        return bookRepository.save(bookMapper.toEntity(bookDTO));
//
//    }
}
