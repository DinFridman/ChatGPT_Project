package com.example.chatgptproject.security.dto;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username, String password) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(username);
        loginUserDTO.setPassword(password);
        return loginUserDTO;
    }
}
