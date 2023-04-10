package com.example.chatgptproject.security.dto;

import java.time.LocalDate;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(String username, String password) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(username);
        loginUserDTO.setPassword(password);
        loginUserDTO.setLoggedInDate(LocalDate.now());
        return loginUserDTO;
    }
}
