package com.bookstore.DTOs;

import com.bookstore.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
This DTO contains the data we allow a user to view about his profile
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMeDTO {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
}
