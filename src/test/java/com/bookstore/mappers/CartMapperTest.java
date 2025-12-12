package com.bookstore.mappers;

import com.bookstore.DTOs.ShoppingCartDTO;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class CartMapperTest {

    private final ShoppingCartMapper cartMapper = Mappers.getMapper(ShoppingCartMapper.class);  // MapStruct auto-generated impl

    @Test
    @DisplayName("toDTO - Map ShoppingCart to ShoppingCartDTO - Success")
    void testToDTO() {
        User user = User.builder().id(10L).build();

        ShoppingCart cart = ShoppingCart.builder()
                .id(5L)
                .user(user)
                .itemCount(3)
                .totalCost(49.99)
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        ShoppingCartDTO dto = cartMapper.toDTO(cart);

        assertEquals(10L, dto.getUserId());
        assertEquals(cart.getItemCount(), dto.getItemCount());
        assertEquals(cart.getTotalCost(), dto.getTotalCost());
        assertEquals(cart.getLastUpdatedAt(), dto.getLastUpdatedAt());
    }

    @Test
    @DisplayName("toEntity - Map a ShoppingCartDTO to Entity - Success")
    void testToEntity(){
        LocalDateTime now = LocalDateTime.now();

        ShoppingCartDTO dto = ShoppingCartDTO.builder()
                .userId(10L)
                .itemCount(2)
                .totalCost(20.0)
                .lastUpdatedAt(now)
                .build();

        ShoppingCart cart = cartMapper.toEntity(dto);

        assertNotNull(cart.getUser());
        assertEquals(10L, cart.getUser().getId());
        assertEquals(dto.getItemCount(), cart.getItemCount());
        assertEquals(dto.getTotalCost(), cart.getTotalCost());
        assertEquals(dto.getLastUpdatedAt(), cart.getLastUpdatedAt());

        // ID must be ignored
        assertNull(cart.getId());

    }

    @Test
    @DisplayName("mapUserIdToUser - Should throw exception when userId is null")
    void testMapUserIdToUser_Null() {
        assertThrows(IllegalArgumentException.class,
                () -> cartMapper.mapUserIdToUser(null));
    }



}
