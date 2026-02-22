package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.PagedResponseDTO;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.ResourceAlreadyExistsException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public PagedResponseDTO<BookDTO> getAllBooks(Authentication auth, Optional<Genre> genre, Pageable pageable) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<Book> page;

        if (isAdmin && genre.isPresent()) {
            page = bookRepository.findAllByGenreAndIsDeletedFalse(genre.get(), pageable);
        } else if (isAdmin && genre.isEmpty()) {
            page = bookRepository.findAllByIsDeletedFalse(pageable);
        } else if (!isAdmin && genre.isPresent()) {
            page = bookRepository.findAllByGenreAndAvailabilityGreaterThanAndIsDeletedFalse(genre.get(), 0, pageable);
        } else {
            page = bookRepository.findByAvailabilityGreaterThanAndIsDeletedFalse(0, pageable);
        }

        List<BookDTO> content = page.getContent().stream()
                .map(bookMapper::toDTO)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
    }


    public BookDTO createBook(BookDTO bookDTO){
        /*
        Checking if publisher provided exists and then if the book already exists
         */
        if (publisherRepository.findById(bookDTO.getPublisherId()).isEmpty())
            throw new ResourceNotFoundException("Publisher with id: " + bookDTO.getPublisherId() + " does not exist");

        if(bookRepository.existsByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor()))
            throw new ResourceAlreadyExistsException("Book with title: " + bookDTO.getTitle()
            + " and author: " + bookDTO.getAuthor() + " already exists.");

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
        bookToDelete.setDeleted(true);

        bookRepository.save(bookToDelete);
    }

}
