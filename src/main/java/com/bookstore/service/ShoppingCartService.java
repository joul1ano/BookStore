package com.bookstore.service;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartDetailsDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.CartItemMapper;
import com.bookstore.mappers.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.ShoppingCartItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ShoppingCartItemsRepository;
import com.bookstore.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemsRepository itemsRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository,
                               ShoppingCartMapper cartMapper,
                               BookRepository bookRepository,
                               ShoppingCartItemsRepository itemsRepository,
                               CartItemMapper cartItemMapper){
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.bookRepository = bookRepository;
        this.itemsRepository = itemsRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public ShoppingCartDTO getCart(Long userId) {
        return cartMapper.toDTO(cartRepository.findByUserId(userId));
    }

    public ShoppingCartDetailsDTO getCartDetails(Long userId){
        ShoppingCart cart = cartRepository.findByUserId(userId);
        ShoppingCartDetailsDTO cartDetailsDTO = ShoppingCartDetailsDTO.builder()
                .userId(userId)
                .itemCount(cart.getItemCount())
                .totalCost(cart.getTotalCost())
                .lastUpdatedAt(cart.getLastUpdatedAt())
                .items(itemsRepository.findAllByShoppingCart_Id(cart.getId()).stream().map(cartItemMapper::toDTO).toList())
                .build();

        return cartDetailsDTO;
    }

    public void cleanUp(Long id){
        ShoppingCart x = cartRepository.findById(id).orElseThrow();
        x.setItemCount(0);
        x.setTotalCost(0);
        cartRepository.save(x);
    }

    public void addItemToCart(Long userId, Long bookId, int quantity){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found"));
        ShoppingCart cart = cartRepository.findByUserId(userId);

        ShoppingCartItem item = ShoppingCartItem.builder().shoppingCart(cart).book(book).quantity(quantity).build();

        itemsRepository.save(item);
        updateCartStatus(cart,quantity,book.getPrice());

    }

    @Transactional
    public void updateItemQuantity(Long userId, Long bookId, Integer newQuantity){
        if(newQuantity > 0){
            ShoppingCart cart = cartRepository.findByUserId(userId);
            ShoppingCartItem item = itemsRepository.findByBook_IdAndShoppingCart_Id(bookId, cart.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            int quantityDiff = newQuantity - item.getQuantity();
            item.setQuantity(newQuantity);

            itemsRepository.save(item);
            updateCartStatus(cart,quantityDiff,item.getBook().getPrice());
        }else{
            removeItemFromCart(userId,bookId);
        }

    }

    @Transactional
    public void removeItemFromCart(Long userId, Long bookId){
        ShoppingCart cart = cartRepository.findByUserId(userId);
        ShoppingCartItem item = itemsRepository.findByBook_IdAndShoppingCart_Id(bookId,cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        itemsRepository.deleteByBook_IdAndShoppingCart_Id(bookId, cart.getId());
        int quantity = item.getQuantity();
        updateCartStatus(cart,(-1)*quantity,item.getBook().getPrice());
    }

    private void updateCartStatus(ShoppingCart cart, int quantity, double price){
        cart.setLastUpdatedAt(LocalDateTime.now());
        cart.setItemCount(cart.getItemCount() + quantity);
        cart.setTotalCost(cart.getTotalCost() + quantity * price);
        cartRepository.save(cart);
    }
}
