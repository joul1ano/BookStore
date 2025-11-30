package com.bookstore.controllers;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.enums.Role;
import com.bookstore.mappers.UserMapper;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /*
    Admin Permission
     */
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));

    }

    /*
    User Permission
     */

    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getMyInfo(@RequestParam Long id){
        return ResponseEntity.ok(userMapper.toUserMeDTO(userService.getUserById(id)));
    }

}
