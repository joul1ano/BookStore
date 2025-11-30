package com.bookstore.DTOs;

import com.bookstore.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/*
This DTO contains the user's data we allow an admin to view
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private double totalAmountSpent;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
