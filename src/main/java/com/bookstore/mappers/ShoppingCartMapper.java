package com.bookstore.mappers;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.model.Publisher;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDTO toDTO(ShoppingCart cart);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user")
    ShoppingCart toEntity(ShoppingCartDTO cartDTO);

    default User mapUserIdToUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = new User();
        user.setId(id);
        return user;
    }

}
