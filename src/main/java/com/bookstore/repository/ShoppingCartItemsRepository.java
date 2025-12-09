package com.bookstore.repository;

import com.bookstore.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemsRepository extends JpaRepository<ShoppingCartItem,Long> {
    Optional<ShoppingCartItem> findByBook_IdAndShoppingCart_Id(Long bookId, Long shoppingCartId);
    boolean existsByBook_IdAndShoppingCart_Id(Long bookId, Long shoppingCartId);
    List<ShoppingCartItem> findAllByShoppingCart_Id(Long shoppingCartId);
}
