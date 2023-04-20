package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.LoginUserDTO;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(username);
        return loginUserDTO;
    }
}
