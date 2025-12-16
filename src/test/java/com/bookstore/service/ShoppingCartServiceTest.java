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
    void testGetCartDetails_Success(){
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

        List<ShoppingCartItemDTO> actual = cartService.getCartDetails(1L);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual,List.of(item1DTO,item2DTO));

    }

    @Test
    @DisplayName("Get cart items - Not items found")
    void testGetCartItems_NoDetailsFound(){
        when(cartRepository.findByUserId(1L))
                .thenReturn(ShoppingCart.builder().id(5L).user(User.builder().id(1L).build()).build());
        when(itemsRepository.findAllByShoppingCart_Id(5L)).thenReturn(List.of());

        List<ShoppingCartItemDTO> actual = cartService.getCartDetails(1L);

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
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Add item to cart - Fail - Book doesn't exist")
    void testAddItemToCart_BookNotFound(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.addItemToCart(1L,1L,1));

        verify(bookRepository).findById(1L);
        verify(cartRepository,never()).findByUserId(5L);
        verify(itemsRepository,never()).save(any());
        verify(cartRepository,never()).save(any());
    }

    @Test
    @DisplayName("updateItemQuanity - Update an item's quantity - Success")
    void testUpdateItemQuantity_Success(){
        ShoppingCart cart = ShoppingCart.builder().id(1L).user(User.builder().id(10L).build()).build();
        ShoppingCartItem item = ShoppingCartItem.builder().id(100L).book(Book.builder().id(5L).price(5.0).build()).quantity(1).build();

        when(cartRepository.findByUserId(10L)).thenReturn(cart);
        when(itemsRepository.findByBook_IdAndShoppingCart_Id(5L,1L)).thenReturn(Optional.of(item));

        cartService.updateItemQuantity(10L,5L,2);

        Assertions.assertEquals(item.getQuantity(),2);

        verify(cartRepository).findByUserId(10L);
        verify(itemsRepository).findByBook_IdAndShoppingCart_Id(5L,1L);
        verify(itemsRepository).save(any());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("updateItemQuanity - Book id doesn't exist - Fail")
    void testUpdateItemQuantity_BookNotFound(){
        ShoppingCart cart = ShoppingCart
                .builder().id(5L).user(User.builder().id(1L).build()).build();

        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(itemsRepository.findByBook_IdAndShoppingCart_Id(10L,5L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.updateItemQuantity(1L,10L,1));

        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository).findByBook_IdAndShoppingCart_Id(10L,5L);
        verify(itemsRepository,never()).save(any());
    }

    @Test
    @DisplayName("updateItemQuanity - New quantity is 0 -> Item deleted")
    void testUpdateItemQuantity_ItemIsRemoved(){
        ShoppingCart cart = ShoppingCart
                .builder().id(5L).user(User.builder().id(1L).build()).build();
        ShoppingCartItem item = ShoppingCartItem.builder()
                .id(100L).book(Book.builder().id(100L).price(5.0).build()).quantity(1).build();
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(itemsRepository.findByBook_IdAndShoppingCart_Id(100L,5L)).thenReturn(Optional.of(item));

        cartService.updateItemQuantity(1L,100L,0);

        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository).findByBook_IdAndShoppingCart_Id(100L,5L);
        verify(itemsRepository,never()).save(any());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Remove item from cart - Success")
    void testRemoveItemFromCart_Success(){
        ShoppingCart cart = ShoppingCart
                .builder().id(5L).user(User.builder().id(1L).build()).build();
        ShoppingCartItem item = ShoppingCartItem.builder()
                .id(100L).book(Book.builder().id(100L).price(5.0).build()).quantity(1).build();
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(itemsRepository.findByBook_IdAndShoppingCart_Id(100L,5L)).thenReturn(Optional.of(item));
        doNothing().when(itemsRepository).deleteByBook_IdAndShoppingCart_Id(100L,5L);

        cartService.removeItemFromCart(1L,100L);

        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository).findByBook_IdAndShoppingCart_Id(100L,5L);
        verify(itemsRepository).deleteByBook_IdAndShoppingCart_Id(100L,5L);
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Remove item from cart - Wrong book id - Fail")
    void testRemoveItemFromCart_BookNotFound(){
        ShoppingCart cart = ShoppingCart
                .builder().id(5L).user(User.builder().id(1L).build()).build();
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(itemsRepository.findByBook_IdAndShoppingCart_Id(100L,5L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.removeItemFromCart(1L,100L));
        Assertions.assertEquals(ex.getMessage(),"Item not found");

        verify(cartRepository).findByUserId(1L);
        verify(itemsRepository,never()).deleteByBook_IdAndShoppingCart_Id(100L,5L);
        verify(cartRepository,never()).save(any());
    }

}
