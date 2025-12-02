package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
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

    public void addBookToFavourites(Long userId, Long bookId){
        favouritesRepository.save(new UserFavourite(null,userId,bookId, LocalDateTime.now()));
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

}
