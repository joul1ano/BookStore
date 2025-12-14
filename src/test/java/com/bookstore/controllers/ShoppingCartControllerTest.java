package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ShoppingCartController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class ShoppingCartControllerTest {
    @MockitoBean
    UserService userService;
    @MockitoBean
    ShoppingCartService cartService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET /users/me/cart - Returns cart info")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testGetCart_Success() throws Exception{
        LocalDateTime updated = LocalDateTime.now();
        ShoppingCartDTO cartDTO = ShoppingCartDTO.builder()
                .userId(1L).totalCost(20.0).itemCount(2).lastUpdatedAt(updated).build();

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        when(cartService.getCart(1L)).thenReturn(cartDTO);

        mockMvc.perform(get("/users/me/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemCount").value(2))
                .andExpect(jsonPath("$.totalCost").value(20.0))
                .andExpect(jsonPath("$.lastUpdatedAt").value(updated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).getCart(1L);
    }

    @Test
    @DisplayName("GET /users/me/cart/items - Returns list of items in cart")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testGetCartItems_Success() throws Exception{
        BookDTO book1 = BookDTO.builder().id(100L).build();
        BookDTO book2 = BookDTO.builder().id(101L).build();
        ShoppingCartItemDTO item1 = ShoppingCartItemDTO.builder()
                .book(book1).quantity(1).build();
        ShoppingCartItemDTO item2 = ShoppingCartItemDTO.builder()
                .book(book2).quantity(1).build();

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        when(cartService.getCartItems(1L)).thenReturn(List.of(item1,item2));

        mockMvc.perform(get("/users/me/cart/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))

                .andExpect(jsonPath("$[0].book.id").value(100L))
                .andExpect(jsonPath("$[0].quantity").value(1))

                .andExpect(jsonPath("$[1].book.id").value(101L))
                .andExpect(jsonPath("$[1].quantity").value(1));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).getCartItems(1L);
    }

    @Test
    @DisplayName("GET /users/me/cart/items - Cart is empty, returns empty list")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testGetCartItems_NoItemsFound() throws Exception{
        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        when(cartService.getCartItems(1L)).thenReturn(List.of());

        mockMvc.perform(get("/users/me/cart/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).getCartItems(1L);
    }

    @Test
    @DisplayName("POST /users/me/cart/items - Adds new item to cart succesfully")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testAddItemToCart_Success() throws Exception{
        AddItemRequest request = AddItemRequest.builder().bookId(100L).quantity(1).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        doNothing().when(cartService).addItemToCart(1L,100L,1);

        mockMvc.perform(post("/users/me/cart/items").contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isOk());

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).addItemToCart(1L,100L,1);

    }

}
