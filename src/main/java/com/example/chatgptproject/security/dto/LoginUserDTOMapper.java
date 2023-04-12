package com.example.chatgptproject.security.dto;


public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(username);
        return loginUserDTO;
    }
}
