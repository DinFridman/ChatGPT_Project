package com.chatgptproject.security.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDetailsDTO {
    private String username;
    private String password;
}
