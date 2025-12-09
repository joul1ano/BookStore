package com.bookstore.service;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.mappers.ShoppingCartMapper;
import com.bookstore.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartMapper cartMapper;

    public ShoppingCartService(ShoppingCartRepository cartRepository, ShoppingCartMapper cartMapper){
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public ShoppingCartDTO getCart(Long userId) {
        return cartMapper.toDTO(cartRepository.findByUserId(userId));
    }
}
