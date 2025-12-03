package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.exceptions.FavouriteAlreadyExistsException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.model.UserFavourite;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserFavouritesRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserFavouritesService {
    private final UserFavouritesRepository favouritesRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public UserFavouritesService(UserFavouritesRepository repository,
                                 UserRepository userRepository,
                                 BookRepository bookRepository,
                                 BookMapper bookMapper){
        this.favouritesRepository = repository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getFavourites(Long userId) {
        List<UserFavourite> favourites = favouritesRepository.findAllByUser_Id(userId);

        return favourites.stream()
                .map(fav -> bookMapper.toDTO(fav.getBook())).toList();
    }

    public void addBookToFavourites(Long userId, Long bookId){
        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found"));

        /* Checking if book already exists in favourites */
        if (favouritesRepository.findByUser_IdAndBook_Id(userId,bookId).isPresent()){
            throw new FavouriteAlreadyExistsException("Book is already in favourites");
        }

        favouritesRepository.save(new UserFavourite(null,user,book,LocalDateTime.now()));
    }

    public void removeFavouriteBook(Long userId, Long bookId){
        UserFavourite favourite = favouritesRepository.findByUser_IdAndBook_Id(userId,bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " is not in favourites"));
        favouritesRepository.delete(favourite);
    }
}
