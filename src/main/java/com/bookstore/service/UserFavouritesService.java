package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.UserFavourite;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserFavouritesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserFavouritesService {
    private final UserFavouritesRepository favouritesRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public UserFavouritesService(UserFavouritesRepository repository,
                                 BookRepository bookRepository,
                                 BookMapper bookMapper){
        this.favouritesRepository = repository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getFavourites(Long userId) {
        List<Long> bookIds = favouritesRepository.findAllByUserId(userId)
                .stream()
                .map(UserFavourite::getBookId)
                .toList();

        return bookRepository.findAllById(bookIds)
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    public void addBookToFavourites(Long userId, Long bookId){
        favouritesRepository.save(new UserFavourite(null,userId,bookId, LocalDateTime.now()));
    }

    public void removeFavouriteBook(Long userId, Long bookId){
        UserFavourite favourite = favouritesRepository.findByUserIdAndBookId(userId,bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " is not in favourites"));
        favouritesRepository.delete(favourite);
    }
}
