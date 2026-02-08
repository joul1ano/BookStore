package com.bookstore.controllers;

import com.bookstore.DTOs.*;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserService userService;
    private final ShoppingCartService cartService;

    public UserController(UserService userService,ShoppingCartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users. Access = [ADMIN]")
    public List<UserDTO> getAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return userService.getAllUsers(pageable.getSort());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns a user's account information. Access = [ADMIN]")
    public ResponseEntity<UserDTO> getUserById(
            @Positive(message = "User id must be a positive number")
            @PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }


    //------------------------   /me   --------------------------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    @Operation(
            summary = "Get my profile",
            description = "Returns account information for the authenticated user"
    )
    public ResponseEntity<UserMeDTO> getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

}
