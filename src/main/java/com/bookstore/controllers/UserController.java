package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.enums.Role;
import com.bookstore.mappers.UserMapper;
import com.bookstore.service.UserFavouritesService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserFavouritesService favouritesService;

    public UserController(UserService userService,UserFavouritesService favouritesService){
        this.favouritesService = favouritesService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        return ResponseEntity.ok(userService.getUserByEmail(userEmail));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("me/favourites")
    public ResponseEntity<List<BookDTO>> getFavouriteBooks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());

        return ResponseEntity.ok(favouritesService.getFavourites(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("me/favourites/{bookId}")
    public ResponseEntity<Void> addBookToFavourites(@PathVariable Long bookId){
        System.out.println(">>>>>CONTROLLER REACHED");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());
        favouritesService.addBookToFavourites(userId,bookId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("me/favourites/{bookId}")
    public ResponseEntity<Void> removeFavouriteByBookId(@PathVariable Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());

        favouritesService.removeFavouriteBook(userId,bookId);

        return ResponseEntity.noContent().build();
    }

}
