package com.bookstore.repository;

import com.bookstore.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemsRepository extends JpaRepository<ShoppingCartItem,Long> {
}
