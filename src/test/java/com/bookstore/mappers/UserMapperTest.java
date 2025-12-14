package com.bookstore.mappers;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.enums.Role;
import com.bookstore.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Map a user to userDTO - Success")
    void testMapUserToUserDTO_Success(){
        User user1 = User.builder()
                .id(1l)
                .name("John")
                .username("john7")
                .password("123")
                .email("johnex@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.of(2025,12,4,16,0))
                .lastLoginAt(LocalDateTime.of(2025,12,4,19,0))
                .build();
        UserDTO userDTO1 = UserDTO.builder()
                .id(1l)
                .name("John")
                .username("john7")
                .email("johnex@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.of(2025,12,4,16,0))
                .lastLoginAt(LocalDateTime.of(2025,12,4,19,0))
                .build();
        UserDTO returnedDTO = userMapper.toAdminDTO(user1);

        Assertions.assertEquals(userDTO1,returnedDTO);
    }

    @Test
    @DisplayName("Map a user to userMeDTO success")
    void testMapUserToUserMeDTO(){
        User user = User.builder()
                .id(1l)
                .name("John")
                .username("john7")
                .password("123")
                .email("johnex@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.of(2025,12,4,16,0))
                .lastLoginAt(LocalDateTime.of(2025,12,4,19,0))
                .build();
        UserMeDTO userDTO = UserMeDTO.builder()
                .name("John")
                .username("john7")
                .email("johnex@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .build();

        UserMeDTO returnedUser = userMapper.toUserMeDTO(user);

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(userDTO,returnedUser);
        
    }
}
