package com.example.chatgptproject.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class LoginUserDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate loggedInDate;
}
