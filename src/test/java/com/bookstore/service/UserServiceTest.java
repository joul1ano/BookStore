package com.bookstore.service;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.enums.Role;
import com.bookstore.mappers.UserMapper;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Get a lsit of all users - Success")
    void testGetAllUsers_Found(){
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
        User user2 = User.builder()
                .id(2l)
                .name("Mario")
                .username("MarioB")
                .password("456")
                .email("marioitaly@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.of(2025,12,1,16,0))
                .lastLoginAt(LocalDateTime.of(2025,12,2,19,0))
                .build();
        List<User> users = Arrays.asList(user1,user2);

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
        UserDTO userDTO2 = UserDTO.builder()
                .id(2l)
                .name("Mario")
                .username("MarioB")
                .email("marioitaly@gmail.com")
                .phoneNumber("699999999")
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.of(2025,12,1,16,0))
                .lastLoginAt(LocalDateTime.of(2025,12,2,19,0))
                .build();
        List<UserDTO> userDTOs = Arrays.asList(userDTO1,userDTO2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toAdminDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toAdminDTO(user2)).thenReturn(userDTO2);

        List<UserDTO> actualUsers = userService.getAllUsers();

        Assertions.assertNotNull(actualUsers);
        Assertions.assertEquals(2, actualUsers.size());
        Assertions.assertIterableEquals(userDTOs, actualUsers);

        verify(userRepository).findAll();
        verify(userMapper,times(2)).toAdminDTO(any());
    }

    @Test
    @DisplayName("Get a lsit of all users - Fail - No users found")
    void testGetAllUsers_NotFound(){
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDTO> users = userService.getAllUsers();

        Assertions.assertTrue(users.isEmpty());
        verify(userRepository).findAll();
        verify(userMapper, never()).toAdminDTO(any());
    }

}
