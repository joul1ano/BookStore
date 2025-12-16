package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartDetailsDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.List;

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
        ShoppingCartDetailsDTO cartDetailsDTO = ShoppingCartDetailsDTO
                .builder()
                .userId(1L)
                .itemCount(2)
                .items(List.of(item1,item2))
                .build();

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        when(cartService.getCartDetails(1L)).thenReturn(cartDetailsDTO);

        mockMvc.perform(get("/users/me/cart/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemCount").value(2))

                .andExpect(jsonPath("$.items[0].book.id").value(100L))
                .andExpect(jsonPath("$.items[0].quantity").value(1))

                .andExpect(jsonPath("$.items[1].book.id").value(101L))
                .andExpect(jsonPath("$.items[1].quantity").value(1));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).getCartDetails(1L);
    }

    @Test
    @DisplayName("GET /users/me/cart/items - Cart is empty, returns empty list")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testGetCartItems_NoItemsFound() throws Exception{
        ShoppingCartDetailsDTO cartDetailsDTO = ShoppingCartDetailsDTO
                .builder()
                .userId(1L)
                .itemCount(0)
                .totalCost(0.0)
                .items(List.of())
                .build();
        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        when(cartService.getCartDetails(1L)).thenReturn(cartDetailsDTO);

        mockMvc.perform(get("/users/me/cart/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemCount").value(0))
                .andExpect(jsonPath("$.totalCost").value(0.0))
                .andExpect(jsonPath("$.items.size()").value(0));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).getCartDetails(1L);
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

    @Test
    @DisplayName("POST /users/me/cart/items - Book doesn't exist")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testAddItemToCart_BookNotFound() throws Exception{
        AddItemRequest request = AddItemRequest.builder().bookId(100L).quantity(1).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        ResourceNotFoundException ex = new ResourceNotFoundException("Book with id: 100 not found");
        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        doThrow(ex).when(cartService).addItemToCart(1L,100L,1);

        mockMvc.perform(post("/users/me/cart/items").contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.message").value("Book with id: 100 not found"));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).addItemToCart(1L,100L,1);
    }

    @Test
    @DisplayName("POST /users/me/cart/items - Quantity given is negative - Fail")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testAddItemToCart_NegativeQuantity() throws Exception{
        AddItemRequest request = AddItemRequest.builder().bookId(100L).quantity(-1).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/me/cart/items").contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.quantity").value("Quantity must be a non-negative number"));

        verify(userService,never()).getUserIdByUsername("tzouliano");
        verify(cartService,never()).addItemToCart(anyLong(),anyLong(),anyInt());
    }

    @Test
    @DisplayName("POST /users/me/cart/items - Both book id and quantity are negative - Fail")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testAddItemToCart_NegativeQuantityAndBookId() throws Exception{
        AddItemRequest request = AddItemRequest.builder().bookId(-1L).quantity(-1).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/me/cart/items").contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.quantity").value("Quantity must be a non-negative number"))
                .andExpect(jsonPath("$.message.bookId").value("Book id must be a positive number"));

        verify(userService,never()).getUserIdByUsername("tzouliano");
        verify(cartService,never()).addItemToCart(anyLong(),anyLong(),anyInt());
    }

    @Test
    @DisplayName("PUT /users/me/cart/items/{bookId} - Update item's quantity - Success")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testUpdateItemQuantity_Success() throws Exception{
        UpdateItemRequest request = UpdateItemRequest.builder().quantity(5).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        doNothing().when(cartService).updateItemQuantity(1L,100L,request.getQuantity());

        mockMvc.perform(put("/users/me/cart/items/{bookId}",100L).contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isOk());

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).updateItemQuantity(1L,100L,request.getQuantity());
    }

    @Test
    @DisplayName("PUT /users/me/cart/items - Book is not found - Fail")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testUpdateItemQuantity_BookNotFound() throws Exception{
        UpdateItemRequest request = UpdateItemRequest.builder().quantity(5).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        ResourceNotFoundException ex = new ResourceNotFoundException("Item not found");

        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        doThrow(ex).when(cartService).updateItemQuantity(1L,100L,request.getQuantity());

        mockMvc.perform(put("/users/me/cart/items/{bookId}",100L).contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item not found"));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).updateItemQuantity(1L,100L,request.getQuantity());
    }

    @Test
    @DisplayName("PUT /users/me/cart/items/{bookId} - Quantity is negative - Fail")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testUpdateItemQuantity_NegativeQuantity() throws Exception{
        UpdateItemRequest request = UpdateItemRequest.builder().quantity(-1).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestInJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/users/me/cart/items/{bookId}",100L).contentType(MediaType.APPLICATION_JSON).content(requestInJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.quantity").value("Quantity must be a non-negative number"));

        verify(userService,never()).getUserIdByUsername("tzouliano");
        verify(cartService,never()).updateItemQuantity(anyLong(),anyLong(),anyInt());
    }

    @Test
    @DisplayName("DELETE /users/me/cart/items/{bookId} - Success")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testDeleteItem_Success() throws Exception{
        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        doNothing().when(cartService).removeItemFromCart(1L,100L);

        mockMvc.perform(delete("/users/me/cart/items/{bookId}",100L))
                .andExpect(status().isNoContent());

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).removeItemFromCart(1L,100L);
    }

    @Test
    @DisplayName("DELETE /users/me/cart/items/{bookId} - Fail")
    @WithMockUser(username = "tzouliano", roles = "USER")
    void testDeleteItem_BookNotFound() throws Exception{
        when(userService.getUserIdByUsername("tzouliano")).thenReturn(1L);
        ResourceNotFoundException ex = new ResourceNotFoundException("Item not found");
        doThrow(ex).when(cartService).removeItemFromCart(1L,100L);

        mockMvc.perform(delete("/users/me/cart/items/{bookId}",100L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item not found"));

        verify(userService).getUserIdByUsername("tzouliano");
        verify(cartService).removeItemFromCart(1L,100L);
    }


}
