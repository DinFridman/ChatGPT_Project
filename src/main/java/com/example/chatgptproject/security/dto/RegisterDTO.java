package com.example.chatgptproject.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
