package com.bookstore.mappers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.ShoppingCartItemDTO;
import com.bookstore.model.Book;
import com.bookstore.model.ShoppingCartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemMapperTest {
   @Mock
   private BookMapper bookMapper;

   @InjectMocks
   CartItemMapperImpl cartItemMapper;

    @Test
    @DisplayName("Mapping ShoppingCartItem to DTO - Success")
    void testToDTO_Success() {
        // Given
        Book book = Book.builder()
                .id(10L)
                .title("Test Book")
                .build();

        BookDTO bookDTO = BookDTO.builder()
                .id(10L)
                .title("Test Book")
                .build();

        ShoppingCartItem item = ShoppingCartItem.builder()
                .id(5L)
                .book(book)
                .quantity(3)
                .build();

        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ShoppingCartItemDTO result = cartItemMapper.toDTO(item);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        assertEquals(bookDTO, result.getBook());
        verify(bookMapper).toDTO(book);
    }
}
