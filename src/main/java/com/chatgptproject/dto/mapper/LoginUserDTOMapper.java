package com.chatgptproject.dto.mapper;

import com.chatgptproject.dto.LoginUserDTO;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username, String password) {
        return LoginUserDTO.builder()
                .username(username)
                .password(password)
                .build();
    }
}
