package com.bookstore.mappers;

import com.bookstore.DTOs.OrderDTO;
import com.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrderMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @Mapping(source = "user.id", target = "userId")
    OrderDTO toDTO(Order order);
}
