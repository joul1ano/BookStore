package com.bookstore.controllers;

import com.bookstore.DTOs.*;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserFavouritesService;
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
    private final UserFavouritesService favouritesService;
    private final ShoppingCartService cartService;

    public UserController(UserService userService,UserFavouritesService favouritesService,ShoppingCartService cartService){
        this.favouritesService = favouritesService;
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



    @PreAuthorize("hasRole('USER')")
    @GetMapping("me/cart")
    public ResponseEntity<ShoppingCartDTO> getCart(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("me/cart/items")
    public ResponseEntity<List<ShoppingCartItemDTO>> getCartItems(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCartItems(userId));
//        cartService.cleanUp(1L);
//        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("me/cart/items")
    public ResponseEntity<Void> addBookToCart(@RequestBody AddItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.addItemToCart(userId,request.getBookId(),request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("me/cart/items/{bookId}")
    public ResponseEntity<Void> updateBookQuantity(@PathVariable Long bookId,@RequestBody UpdateItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.updateItemQuantity(userId,bookId,request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("me/cart/items/{bookId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.removeItemFromCart(userId,bookId);
        return ResponseEntity.noContent().build();
    }


}
