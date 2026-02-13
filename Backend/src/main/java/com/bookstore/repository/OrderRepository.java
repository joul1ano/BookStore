package com.bookstore.repository;

import com.bookstore.enums.OrderStatus;
import com.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByUser_Id(Long userId);
    List<Order> findAllByUser_Username(String username);
    List<Order> findAllByUser_UsernameAndStatus(String username, OrderStatus status);
    List<Order> findAllByStatus(OrderStatus status);
}
