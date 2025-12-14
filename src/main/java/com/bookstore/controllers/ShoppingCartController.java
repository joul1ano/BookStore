package com.bookstore.controllers;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.DTOs.requests.AddItemRequest;
import com.bookstore.DTOs.requests.UpdateItemRequest;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me/cart")
public class ShoppingCartController {
    private final UserService userService;
    private final ShoppingCartService cartService;

    public ShoppingCartController(UserService userService,ShoppingCartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<ShoppingCartDTO> getCart(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/items")
    public ResponseEntity<List<ShoppingCartItemDTO>> getCartItems(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(cartService.getCartItems(userId));
//        cartService.cleanUp(1L);
//        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/items")
    public ResponseEntity<Void> addBookToCart(@RequestBody AddItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.addItemToCart(userId,request.getBookId(),request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{bookId}")
    public ResponseEntity<Void> updateBookQuantity(@PathVariable Long bookId,@RequestBody UpdateItemRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.updateItemQuantity(userId,bookId,request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        cartService.removeItemFromCart(userId,bookId);
        return ResponseEntity.noContent().build();
    }
}
