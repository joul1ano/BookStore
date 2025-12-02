package com.bookstore.service;

import com.bookstore.model.UserFavourite;
import com.bookstore.repository.UserFavouritesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserFavouritesService {
    private final UserFavouritesRepository favouritesRepository;

    public UserFavouritesService(UserFavouritesRepository repository){
        this.favouritesRepository = repository;
    }

    public void addBookToFavourites(Long userId, Long bookId){
        favouritesRepository.save(new UserFavourite(null,userId,bookId, LocalDateTime.now()));
    }
}
