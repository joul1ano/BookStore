package com.bookstore.controllers;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.enums.Role;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.service.UserFavouritesService;
import com.bookstore.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


}
