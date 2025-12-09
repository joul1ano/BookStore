package com.bookstore.repository;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findById(Long Id);
    ShoppingCart findByUserId(Long userId);

}
