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
    @Autowired
    private final UserMapper userMapper;

    public UserController(UserService userService,UserFavouritesService favouritesService, UserMapper userMapper){
        this.favouritesService = favouritesService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /*
    Admin Permission
     */
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

    /*
    User Permission
     */

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        return ResponseEntity.ok(userService.getUserByEmail(userEmail));
    }

//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("me/favourites")
//    public ResponseEntity<List<BookDTO>> getFavouriteBooks(){
//        return
//    }

    //@PreAuthorize("hasRole('USER')")
    @PostMapping("me/favourites")
    public ResponseEntity<Void> addBookToFavourites(@RequestBody Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Long userId = userService.getUserIdByEmail(auth.getName());
        favouritesService.addBookToFavourites(userId,bookId);
        return ResponseEntity.noContent().build();

    }

}
