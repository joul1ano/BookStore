package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.enums.Role;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Publisher;
import com.bookstore.model.User;
import com.bookstore.model.UserFavourite;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserFavouritesRepository;
import com.bookstore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserFavouritesServiceTest {
    @Mock
    private UserFavouritesRepository favouritesRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private UserFavouritesService userFavouritesService;

    @Test
    @DisplayName("Get the authenticated user's list of favourite books - Success")
    void testGetAllFavourites_Success() {
        User user = User.builder()
                .id(1L)
                .username("john7")
                .build();

        Book book1 = Book.builder().id(7L).title("title 1").build();
        Book book2 = Book.builder().id(8L).title("title 2").build();
        BookDTO book1DTO = BookDTO.builder().id(7L).title("title 1").build();
        BookDTO book2DTO = BookDTO.builder().id(8L).title("title 2").build();


        List<UserFavourite> favourites = List.of(
                UserFavourite.builder().user(user).book(book1).build(),
                UserFavourite.builder().user(user).book(book2).build()
        );

        when(favouritesRepository.findAllByUser_Id(1L)).thenReturn(favourites);
        when(bookMapper.toDTO(book1)).thenReturn(book1DTO);
        when(bookMapper.toDTO(book2)).thenReturn(book2DTO);

        List<BookDTO> returned = userFavouritesService.getFavourites(1L);

        Assertions.assertEquals(List.of(book1DTO, book2DTO), returned);

        verify(favouritesRepository).findAllByUser_Id(1L);
        verify(bookMapper, times(2)).toDTO(any());
    }

    @Test
    @DisplayName("Get the authenticated user's list of favourite books - Fail - No books found")
    void testGetAllFavourites_NotFound(){
        when(favouritesRepository.findAllByUser_Id(1L)).thenReturn(List.of());

        List<BookDTO> returned = userFavouritesService.getFavourites(1L);

        Assertions.assertTrue(returned.isEmpty());

        verify(favouritesRepository).findAllByUser_Id(1L);
        verify(bookMapper,never()).toDTO(any());
    }

    @Test
    @DisplayName("Add a book to favourites - Success")
        void testAddBookToFavourites_Success(){
        User user = User.builder().id(1L).username("john7").build();
        Book book1 = Book.builder().id(7L).title("title 1").build();
        UserFavourite fav = UserFavourite.builder().user(user).book(book1).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(7L)).thenReturn(Optional.of(book1));
        when(favouritesRepository.findByUser_IdAndBook_Id(1L,7L)).thenReturn(Optional.empty());
        when(favouritesRepository.save(fav)).thenReturn(fav);

        userFavouritesService.addBookToFavourites(1L,7L);

       verify(userRepository.findById(1L));
       verify(bookRepository).findById(7L);
       verify(favouritesRepository).findByUser_IdAndBook_Id(1L,7L);
       verify(favouritesRepository).save(fav);


    }


}
