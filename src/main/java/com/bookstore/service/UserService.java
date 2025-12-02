package com.bookstore.service;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.enums.Role;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.UserMapper;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(userMapper::toAdminDTO).toList();
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toAdminDTO)
                .orElseThrow(()-> new ResourceNotFoundException("User with id: " + id + " not found"));
    }

    public UserMeDTO getUserByEmail(String email){
        return userMapper.toUserMeDTO(userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found")));
    }

    public Long getUserIdByEmail(String email){
        var user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User with email: " + email + " not found"));
        return user.getId();

    }


}
