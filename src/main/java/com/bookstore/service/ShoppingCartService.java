package com.bookstore.service;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.ShoppingCartItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ShoppingCartItemsRepository;
import com.bookstore.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemsRepository itemsRepository;
    private final ShoppingCartMapper cartMapper;
    private final BookRepository bookRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository,
                               ShoppingCartMapper cartMapper,
                               BookRepository bookRepository,
                               ShoppingCartItemsRepository itemsRepository){
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.bookRepository = bookRepository;
        this.itemsRepository = itemsRepository;
    }

    public ShoppingCartDTO getCart(Long userId) {
        return cartMapper.toDTO(cartRepository.findByUserId(userId));
    }

    public void addItemToCart(Long userId, Long bookId, int quantity){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found"));
        ShoppingCart cart = cartRepository.findByUserId(userId);
        ShoppingCartItem item = ShoppingCartItem.builder().shoppingCart(cart).book(book).quantity(quantity).build();

        itemsRepository.save(item);
        updateCartStatus(cart,item);
    }

    public void updateCartStatus(ShoppingCart cart, ShoppingCartItem item){
        cart.setLastUpdatedAt(LocalDateTime.now());
        cart.setItemCount(cart.getItemCount() + item.getQuantity());
        cart.setTotalCost(cart.getTotalCost() + item.getBook().getPrice()* item.getQuantity());
        cartRepository.save(cart);
    }
}
