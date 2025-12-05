package com.bookstore.mappers;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // 1) Map User → UserDTO  (admin)
    UserDTO toAdminDTO(User user);

    // 2) Map User → UserMeDTO (user seeing his own profile)
    UserMeDTO toUserMeDTO(User user);
}