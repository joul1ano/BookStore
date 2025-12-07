package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.enums.Genre;
import com.bookstore.enums.Role;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.service.UserFavouritesService;
import com.bookstore.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.server.RSocketServerException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;




@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class UserControllerTest {
    @MockitoBean
    UserService userService;

    @MockitoBean
    UserFavouritesService userFavouritesService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /users - Success (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers_Success() throws Exception {

        List<UserDTO> mockUsers = List.of(
                UserDTO.builder()
                        .id(1L)
                        .name("Alice Johnson")
                        .username("alicej")
                        .email("alice@example.com")
                        .phoneNumber("555-1001")
                        .role(Role.USER)
                        .totalAmountSpent(249.99)
                        .createdAt(LocalDateTime.of(2024,1,10,10,0))
                        .lastLoginAt(LocalDateTime.of(2024,11,10,8,45))
                        .build(),
                UserDTO.builder()
                        .id(2L)
                        .name("Bob Smith")
                        .username("bsmith")
                        .email("bob@example.com")
                        .phoneNumber("555-1002")
                        .role(Role.ADMIN)
                        .totalAmountSpent(99.5)
                        .createdAt(LocalDateTime.of(2024,2,15,14,30))
                        .lastLoginAt(LocalDateTime.of(2024,11,20,12,10))
                        .build()
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))

                // First user
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice Johnson"))
                .andExpect(jsonPath("$[0].username").value("alicej"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("555-1001"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[0].totalAmountSpent").value(249.99))
                .andExpect(jsonPath("$[0].createdAt").value("2024-01-10T10:00:00"))
                .andExpect(jsonPath("$[0].lastLoginAt").value("2024-11-10T08:45:00"))

                // Second user
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob Smith"))
                .andExpect(jsonPath("$[1].username").value("bsmith"))
                .andExpect(jsonPath("$[1].email").value("bob@example.com"))
                .andExpect(jsonPath("$[1].phoneNumber").value("555-1002"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].totalAmountSpent").value(99.5))
                .andExpect(jsonPath("$[1].createdAt").value("2024-02-15T14:30:00"))
                .andExpect(jsonPath("$[1].lastLoginAt").value("2024-11-20T12:10:00"));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("GET /users - No users found (empty list)")
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers_NoUsersFound() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));  // empty JSON array

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("GET /users/{id} - Success")
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_Success() throws Exception{
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("Alice Johnson")
                .username("alicej")
                .email("alice@example.com")
                .phoneNumber("555-1001")
                .role(Role.USER)
                .totalAmountSpent(249.99)
                .createdAt(LocalDateTime.of(2024,1,10,10,0))
                .lastLoginAt(LocalDateTime.of(2024,11,10,8,45))
                .build();

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.username").value("alicej"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("555-1001"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.totalAmountSpent").value(249.99))
                .andExpect(jsonPath("$.createdAt").value("2024-01-10T10:00:00"))
                .andExpect(jsonPath("$.lastLoginAt").value("2024-11-10T08:45:00"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("GET /users/{id} - Fail - User not found")
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_NotFound() throws Exception{
        ResourceNotFoundException exception = new ResourceNotFoundException("User with id: 1 not found");
        doThrow(exception).when(userService).getUserById(1L);

        mockMvc.perform(get("/users/{id}",1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User with id: 1 not found"));
    }

    @Test
    @WithMockUser(username = "john", roles = "USER")
    @DisplayName("GET /users/me - Success")
    void testGetMe_Success() throws Exception {

        UserMeDTO dto = UserMeDTO.builder()
                .name("John")
                .username("john")
                .email("john@mail.com")
                .phoneNumber("123456789")
                .role(Role.USER)
                .build();

        when(userService.getUserByUsername("john")).thenReturn(dto);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@mail.com"));

        verify(userService).getUserByUsername("john");
    }

    @Test
    @DisplayName("GET /users/favourites - Success")
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
    @DisplayName("POST /users/me/favourites/{bookId} - Success - Add book to favourites")
    @WithMockUser(username = "alicej", roles = "USER")
    void testAddBookToFavourites_Success() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);
        doNothing().when(userFavouritesService).addBookToFavourites(1L,7L);

        mockMvc.perform(post("/users/me/favourites/{bookId}",7L))
                .andExpect(status().isNoContent());

        verify(userService).getUserIdByUsername("alicej");
        verify(userFavouritesService).addBookToFavourites(1L,7L);
    }

    @Test
    @DisplayName("POST /users/me/favourites/{bookId} - Fail - Book doesn't exist")
    @WithMockUser(username = "alicej", roles = "USER")
    void testAddBookToFavourites_Fail() throws Exception{
        when(userService.getUserIdByUsername("alicej")).thenReturn(1L);

        ResourceNotFoundException ex = new ResourceNotFoundException("Book with id: 7 not found");
        doThrow(ex).when(userFavouritesService).addBookToFavourites(1L,7L);

        mockMvc.perform(post("/users/me/favourites/{bookId}",7L))
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







}
