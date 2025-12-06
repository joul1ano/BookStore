package com.bookstore.service;

import com.bookstore.DTOs.UserDTO;
import com.bookstore.DTOs.UserMeDTO;
import com.bookstore.enums.Role;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.UserMapper;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
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
import java.util.Optional;

import static org.mockito.Mockito.*;

//TODO CHANGE THE TESTS FROM GET USER BY EMAIL --> GET USER BY USERNAME
//TODO CHANGE THE TESTS FROM GET USER BY EMAIL --> GET USER BY USERNAME
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

    @Test
    @DisplayName("Get user by id - Success")
    void getUserById_Found(){
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
        UserDTO userDTO = UserDTO.builder()
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

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toAdminDTO(user)).thenReturn(userDTO);

        UserDTO returnedUser = userService.getUserById(1L);

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(userDTO,returnedUser);

        verify(userRepository).findById(1L);
        verify(userMapper).toAdminDTO(user);
    }

    @Test
    @DisplayName("Get user by id - Fail - Not found")
    void testGetUserById_NotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));

        verify(userRepository).findById(1L);
        verify(userMapper, never()).toAdminDTO(any());
    }
    //TODO CHANGE THE TESTS FROM GET USER BY EMAIL --> GET USER BY USERNAME
    @Test
    @DisplayName("Get user by email - Success")
    void testGetUserByEmail_Found(){
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

        when(userRepository.findByUsername("johnex@gmail.com")).thenReturn(Optional.of(user));
        when(userMapper.toUserMeDTO(user)).thenReturn(userDTO);

        UserMeDTO returnedUser = userService.getUserByEmail("johnex@gmail.com");

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(userDTO,returnedUser);

        verify(userRepository).findByUsername("johnex@gmail.com");
        verify(userMapper).toUserMeDTO(user);
    }

    @Test
    @DisplayName("Get user by email - Fail - Not found")
    void testGetUserByEmail_NotFound(){
        when(userRepository.findByUsername("johnex@gmail.com")).thenReturn(Optional.empty());


        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserIdByEmail("johnex@gmail.com"));

        verify(userRepository).findByUsername("johnex@gmail.com");
        verify(userMapper,never()).toUserMeDTO(any());

    }

    @Test
    @DisplayName("Get a user's id by username - Success")
    void testGetUserIdByEmail_Found(){
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

        when(userRepository.findByUsername("john7")).thenReturn(Optional.of(user));

        Long id = userService.getUserIdByEmail("johnex@gmail.com");

        Assertions.assertEquals(1L,id);

        verify(userRepository).findByUsername("johnex@gmail.com");
    }
    /*
    It doesn't make sense to write a negative test for this case, because when the method we test runs,
    the user is already authenticated, which means he has logged in with a valid email, and he obviously has a valid user id
     */

}
