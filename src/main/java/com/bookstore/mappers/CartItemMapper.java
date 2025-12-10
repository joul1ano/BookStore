package com.bookstore.mappers;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.model.ShoppingCartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CartItemMapper {
    //@Mapping(source = "shoppingCart.id", target = "shoppingCartId")
    @Mapping(source = "book", target = "book")
    ShoppingCartItemDTO toDTO(ShoppingCartItem item);



}