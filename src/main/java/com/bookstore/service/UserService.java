package com.bookstore.service;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.UserMapper;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<UserDTO> getAllUsers(Sort sort){
        if (sort == null || sort.isUnsorted()) {
            sort = Sort.by(Sort.Direction.ASC, "id");
        }

        return userRepository.findAll(sort)
                .stream()
                .map(userMapper::toAdminDTO)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toAdminDTO)
                .orElseThrow(()-> new ResourceNotFoundException("User with id: " + id + " not found"));
    }

    public UserMeDTO getUserByUsername(String username){
        return userMapper.toUserMeDTO(userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found")));
    }

    public Long getUserIdByUsername(String username){
        var user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User with email: " + username + " not found"));
        return user.getId();

    }


}
