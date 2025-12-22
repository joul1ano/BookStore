package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    @Autowired
    private BookMapper bookMapper;

    public BookService (BookRepository bookRepository, BookMapper bookMapper,  PublisherRepository publisherRepository){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.publisherRepository = publisherRepository;
    }

    public List<BookDTO> getAllBooks(Authentication auth)
    {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if(isAdmin){
            return bookRepository.findAll().stream().map(bookMapper::toDTO).toList();
        }
        return bookRepository.findByAvailabilityGreaterThan(0).stream().map(bookMapper::toDTO).toList();
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
    }


    public BookDTO createBook(BookDTO bookDTO){
        if (publisherRepository.findById(bookDTO.getPublisherId()).isEmpty())
            throw new ResourceNotFoundException("Publisher with id: " + bookDTO.getPublisherId() + " does not exist");
        return bookMapper.toDTO(bookRepository.save(bookMapper.toEntity(bookDTO)));

    }

    public BookDTO updateBookById(Long id,BookDTO bookDTO){
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setNumberOfPages(bookDTO.getNumberOfPages());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setAvailability(bookDTO.getAvailability());
        existingBook.setPublisher(publisherRepository.findById(bookDTO.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with id: " + bookDTO.getPublisherId() + " does not exist")));

        return bookMapper.toDTO(bookRepository.save(existingBook));

    }

    public void deleteBookById(Long id){
        Book bookToDelete = bookRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));

        bookRepository.delete(bookToDelete);
    }

}
