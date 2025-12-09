package com.bookstore.service;

import com.bookstore.DTOs.ShoppingCartDTO;
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

    public List<ShoppingCartItemDTO> getCartItems(Long userId){
        Long cartId = cartRepository.findByUserId(userId).getId();

        return itemsRepository.findAllByShoppingCart_Id(cartId).stream().map(cartItemMapper::toDTO).toList();
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

        ShoppingCartItem item = new ShoppingCartItem();
        if(itemsRepository.existsByBook_IdAndShoppingCart_Id(bookId, cart.getId())){
            item = itemsRepository.findByBook_IdAndShoppingCart_Id(bookId, cart.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
            item.setQuantity(item.getQuantity() + quantity);
        }else {
            item = ShoppingCartItem.builder().shoppingCart(cart).book(book).quantity(quantity).build();
        }

        itemsRepository.save(item);
        updateCartStatus(cart,quantity,book.getPrice());
    }

    public void updateCartStatus(ShoppingCart cart, int quantity, double price){
        cart.setLastUpdatedAt(LocalDateTime.now());
        cart.setItemCount(cart.getItemCount() + quantity);
        cart.setTotalCost(cart.getTotalCost() + quantity * price);
        cartRepository.save(cart);
    }
}
