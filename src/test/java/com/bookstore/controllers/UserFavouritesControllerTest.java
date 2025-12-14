package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.requests.AddFavouriteRequest;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.FavouriteAlreadyExistsException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.service.UserFavouritesService;
import com.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserFavouritesController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class UserFavouritesControllerTest {
    @MockitoBean
    UserService userService;

    @MockitoBean
    UserFavouritesService userFavouritesService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /users/me/favourites - Success")
    @WithMockUser(username = "alicej", roles = "USER")
    void testGetUserFavourites_Success() throws Exception{
        BookDTO book1 = BookDTO.builder()
                .id(7L)
                .title("title 1")
                .author("Author 1")
                .description("Descr 1")
                .genre(Genre.FICTION)
                .numberOfPages(221)
                .price(14.5)
                .availability(220)
                .publisherId(8L)
                .build();
        BookDTO book2 = BookDTO.builder()
                .id(8L)
                .title("title 2")
                .author("Author 2")
                .description("Descr 2")
                .genre(Genre.FICTION)
                .numberOfPages(456)
                .price(16.4)
                .availability(100)
                .publisherId(8L)
                .build();

        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        when(userFavouritesService.getFavourites(1L)).thenReturn(List.of(book1,book2));

        mockMvc.perform(get("/users/me/favourites"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(7L))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].author").value("Author 1"))
                .andExpect(jsonPath("$[0].genre").value("FICTION"))
                .andExpect(jsonPath("$[0].price").value(14.5))

                .andExpect(jsonPath("$[1].id").value(8L))
                .andExpect(jsonPath("$[1].title").value("title 2"))
                .andExpect(jsonPath("$[1].author").value("Author 2"))
                .andExpect(jsonPath("$[1].genre").value("FICTION"))
                .andExpect(jsonPath("$[1].price").value(16.4));

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).getFavourites(1L);

    }

    @Test
    @DisplayName("GET /users/me/favourites - Fail - No favourites found")
    @WithMockUser(username = "alicej", roles = "USER")
    void testGetUserFavourites_NoFavourites() throws Exception {
        // User "alicej" has id=1
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);

        when(userFavouritesService.getFavourites(1L)).thenReturn(List.of());

        mockMvc.perform(get("/users/me/favourites"))
                .andExpect(status().isOk())  // still 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));  // array is empty

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).getFavourites(1L);
    }

    @Test
    @DisplayName("POST /users/me/favourites - Success - Add book to favourites")
    @WithMockUser(username = "alicej", roles = "USER")
    void testAddBookToFavourites_Success() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        doNothing().when(userFavouritesService).addBookToFavourites(1L,7L);

        AddFavouriteRequest request = AddFavouriteRequest.builder().bookId(7L).build();
        ObjectMapper objMapper = new ObjectMapper();
        String bookIdInJson = objMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/me/favourites").contentType(MediaType.APPLICATION_JSON)
                        .content(bookIdInJson))
                .andExpect(status().isOk());

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).addBookToFavourites(1L,7L);
    }

    @Test
    @DisplayName("POST /users/me/favourites/{bookId} - Fail - Book is already in favourites")
    @WithMockUser(username = "alicej", roles = "USER")
    void testAddBookToFavourites_AlreadyExists() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        Exception ex = new FavouriteAlreadyExistsException("Book is already in favourites");
        doThrow(ex).when(userFavouritesService).addBookToFavourites(1L,7L);

        AddFavouriteRequest request = AddFavouriteRequest.builder().bookId(7L).build();
        ObjectMapper objMapper = new ObjectMapper();
        String bookIdInJson = objMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/me/favourites").contentType(MediaType.APPLICATION_JSON)
                        .content(bookIdInJson))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book is already in favourites"));
    }

    @Test
    @DisplayName("POST /users/me/favourites/{bookId} - Fail - Book doesn't exist")
    @WithMockUser(username = "alicej", roles = "USER")
    void testAddBookToFavourites_Fail() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);

        ResourceNotFoundException ex = new ResourceNotFoundException("Book with id: 7 not found");
        doThrow(ex).when(userFavouritesService).addBookToFavourites(1L,7L);

        AddFavouriteRequest request = AddFavouriteRequest.builder().bookId(7L).build();
        ObjectMapper objMapper = new ObjectMapper();
        String bookIdInJson = objMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/me/favourites").contentType(MediaType.APPLICATION_JSON)
                        .content(bookIdInJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 7 not found"));

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).addBookToFavourites(1L,7L);
    }

    @Test
    @DisplayName("DELETE /users/me/favourites/{bookId} - Success")
    @WithMockUser(username = "alicej", roles = "USER")
    void testRemoveBookFromFavourites_Success() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        doNothing().when(userFavouritesService).removeFavouriteBook(1L,7L);

        mockMvc.perform(delete("/users/me/favourites/{id}",7L))
                .andExpect(status().isNoContent());

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).removeFavouriteBook(1L,7L);
    }

    @Test
    @DisplayName("DELETE /users/me/favourites/{bookId} - Fail")
    @WithMockUser(username = "alicej", roles = "USER")
    void testRemoveBookFromFavourites_NotFound() throws Exception {
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        ResourceNotFoundException ex = new ResourceNotFoundException("Book with id: 7 is not in favourites");
        doThrow(ex).when(userFavouritesService).removeFavouriteBook(1L,7L);

        mockMvc.perform(delete("/users/me/favourites/{bookId}",7L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 7 is not in favourites"));

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).removeFavouriteBook(1L,7L);
    }
}
