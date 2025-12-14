package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.requests.AddFavouriteRequest;
import com.bookstore.service.UserFavouritesService;
import com.bookstore.service.UserService;
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
@RequestMapping("/users/me/favourites")
@Validated
public class UserFavouritesController {
    private final UserService userService;
    private final UserFavouritesService favouritesService;

    public UserFavouritesController(UserService userService, UserFavouritesService favouritesService){
        this.userService = userService;
        this.favouritesService = favouritesService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getFavouriteBooks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        return ResponseEntity.ok(favouritesService.getFavourites(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Void> addBookToFavourites(@Valid @RequestBody AddFavouriteRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());
        favouritesService.addBookToFavourites(userId, request.getBookId());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeFavouriteByBookId(
            @PathVariable
            @Positive(message = "Book id must be a positive number")
            Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByUsername(auth.getName());

        favouritesService.removeFavouriteBook(userId,bookId);

        return ResponseEntity.noContent().build();
    }
}
