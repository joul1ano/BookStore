package com.bookstore.controllers;

import com.bookstore.DTOs.*;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    private final ShoppingCartService cartService;

    public UserController(UserService userService,ShoppingCartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Positive(message = "User id must be a positive number")
            @PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }


    //------------------------   /me   --------------------------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

}
