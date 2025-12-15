package com.bookstore.mappers;

import com.bookstore.DTOs.AdminOrderItemDTO;
import com.bookstore.DTOs.UserOrderItemDTO;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.crypto.spec.PSource;

@Mapper(componentModel = "spring",uses = {OrderMapper.class, BookMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "bookId", target = "book.id")
    OrderItem toEntity(AdminOrderItemDTO adminOrderItemDTO);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "book.id", target = "bookId")
    AdminOrderItemDTO toAdminOrderItemDTO(OrderItem orderItem);

    @Mapping(source = "book.id", target = "bookId")
    UserOrderItemDTO toUserOrderItemDTO(OrderItem orderItem);
}
