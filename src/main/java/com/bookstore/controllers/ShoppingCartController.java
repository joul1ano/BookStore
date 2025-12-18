package com.bookstore.controllers;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartDetailsDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me/cart")
@Validated
@Tag(name = "Cart", description = "Operations related to cart")
public class ShoppingCartController {
    private final UserService userService;
    private final ShoppingCartService cartService;

    public ShoppingCartController(UserService userService,ShoppingCartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(
            summary = "Get my cart",
            description = "Returns a summary of the cart for the authenticated user. Access = [USER]"
    )
    public ResponseEntity<ShoppingCartDTO> getCart(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/items")
    @Operation(
            summary = "Get cart items",
            description = "Returns full cart details for the authenticated user. Access = [USER]"
    )
    public ResponseEntity<ShoppingCartDetailsDTO> getCartItems(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCartDetails(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/items")
    @Operation(
            summary = "Add book to cart",
            description = "Adds a book to the authenticated user's cart. Access = [USER]"
    )
    public ResponseEntity<Void> addBookToCart(@Valid @RequestBody AddItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.addItemToCart(userId,request.getBookId(),request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{bookId}")
    @Operation(summary = "Update a book's quantity", description = "Access = [USER]")
    public ResponseEntity<Void> updateBookQuantity(
            @Positive(message = "Book id must be a positive number")
            @PathVariable Long bookId,
            @Valid @RequestBody
            UpdateItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.updateItemQuantity(userId,bookId,request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{bookId}")
    @Operation(
            summary = "Remove book from cart",
            description = "Completely removes a book from the authenticated user's cart. Access = [USER]"
    )
    public ResponseEntity<Void> removeBookFromCart(
            @Positive(message = "Book id must be a positive number")
            @PathVariable
            Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.removeItemFromCart(userId,bookId);
        return ResponseEntity.noContent().build();
    }
}
