package com.bookstore.mappers;

import com.bookstore.DTOs.OrderItemDTO;
import com.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {OrderMapper.class, BookMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
