package com.bookstore.mappers;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

//TODO USE MAPSTRUCT
@Component
public class UserMapper {
    public UserDTO toAdminDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .totalAmountSpent(user.getTotalAmountSpent())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt()).build();
    }

    public UserMeDTO toUserMeDTO(UserDTO user){
        return UserMeDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole()).build();
    }
}
