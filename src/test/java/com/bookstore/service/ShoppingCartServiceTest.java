package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.CartItemMapper;
import com.bookstore.mappers.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.ShoppingCartItem;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ShoppingCartItemsRepository;
import com.bookstore.repository.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository cartRepository;
    @Mock
    private ShoppingCartMapper cartMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartItemsRepository itemsRepository;
    @Mock
    private CartItemMapper itemMapper;
    @InjectMocks
    private ShoppingCartService cartService;

    @Test
    @DisplayName("Get a user's cart - Success")
    void testGetUserCart_Success(){
        LocalDateTime updated = LocalDateTime.now();
        ShoppingCart cart = ShoppingCart.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .itemCount(3)
                .totalCost(50.0)
                .lastUpdatedAt(updated)
                .build();
        ShoppingCartDTO cartDTO = ShoppingCartDTO.builder()
                .userId(1L)
                .itemCount(3)
                .totalCost(50.0)
                .lastUpdatedAt(updated)
                .build();
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        ShoppingCartDTO returnedCart = cartService.getCart(1L);

        Assertions.assertNotNull(returnedCart);
        Assertions.assertEquals(returnedCart,cartDTO);

        verify(cartRepository).findByUserId(1L);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    @DisplayName("Get cart items - Success")
    void testGetCartItems_Success(){
        ShoppingCartItem item1 = ShoppingCartItem.builder()
                .id(20L)
                .shoppingCart(ShoppingCart.builder().id(5L).build())
                .book(Book.builder().id(100L).build())
                .quantity(3)
                .build();
        ShoppingCartItem item2 = ShoppingCartItem.builder()
                .id(21L)
                .shoppingCart(ShoppingCart.builder().id(5L).build())
                .book(Book.builder().id(200L).build())
                .quantity(1)
                .build();
        ShoppingCartItemDTO item1DTO = ShoppingCartItemDTO.builder()
                .book(BookDTO.builder().id(100L).build())
                .quantity(3)
                .build();
        ShoppingCartItemDTO item2DTO = ShoppingCartItemDTO.builder()
                .book(BookDTO.builder().id(200L).build())
                .quantity(1)
                .build();
        when(cartRepository.findByUserId(1L))
                .thenReturn(ShoppingCart.builder().id(5L).user(User.builder().id(1L).build()).build());
        when(itemsRepository.findAllByShoppingCart_Id(5L)).thenReturn(List.of(item1,item2));
        when(itemMapper.toDTO(item1)).thenReturn(item1DTO);
        when(itemMapper.toDTO(item2)).thenReturn(item2DTO);

        List<ShoppingCartItemDTO> actual = cartService.getCartItems(1L);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual,List.of(item1DTO,item2DTO));

    }

    @Test
    @DisplayName("Get cart items - Not items found")
    void testGetCartItems_NoItemsFound(){
        when(cartRepository.findByUserId(1L))
                .thenReturn(ShoppingCart.builder().id(5L).user(User.builder().id(1L).build()).build());
        when(itemsRepository.findAllByShoppingCart_Id(5L)).thenReturn(List.of());

        List<ShoppingCartItemDTO> actual = cartService.getCartItems(1L);

        Assertions.assertTrue(actual.isEmpty());
        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository).findAllByShoppingCart_Id(5L);
        verify(itemMapper,never()).toDTO(any());
    }

    @Test
    @DisplayName("Add item to cart - Success")
    void testAddItemToCart_Success(){
        Book book = Book.builder().id(10L).title("abc").price(10.0).build();
        ShoppingCart cart = ShoppingCart.builder().id(5L).user(User.builder().id(1L).build()).build();
        ShoppingCartItem item = ShoppingCartItem.builder().shoppingCart(cart).book(book).quantity(1).build();
        ShoppingCartItem savedItem = ShoppingCartItem.builder().id(50L).shoppingCart(cart).book(book).quantity(1).build();

        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(itemsRepository.save(item)).thenReturn(savedItem);

        cartService.addItemToCart(1L,10L,1);

        verify(bookRepository).findById(10L);
        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository).save(item);
    }

    @Test
    @DisplayName("Add item to cart - Fail - Book doesn't exist")
    void testAddItemToCart_BookNotFound(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.addItemToCart(1L,1L,1));

        verify(bookRepository).findById(1L);
        verify(cartRepository,never()).findByUserId(5L);
        verify(itemsRepository,never()).save(any());
    }

    @Test
    @DisplayName("updateItemQuanity - Update an item's quantity - Success")
    void testUpdateItemQuantity_Success(){

    }

}
