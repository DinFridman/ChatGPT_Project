package com.example.chatgptproject.dto.mapper;

import com.example.chatgptproject.dto.LoginUserDTO;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username, String password) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(username);
        loginUserDTO.setPassword(password);
        return loginUserDTO;
    }
}
