package com.example.chatgptproject.dto.mapper;

import com.example.chatgptproject.dto.LoginUserDTO;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username, String password) {
        return LoginUserDTO.builder()
                .username(username)
                .password(password)
                .build();
    }
}
