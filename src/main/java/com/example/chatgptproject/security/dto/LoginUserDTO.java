package com.example.chatgptproject.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginUserDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
