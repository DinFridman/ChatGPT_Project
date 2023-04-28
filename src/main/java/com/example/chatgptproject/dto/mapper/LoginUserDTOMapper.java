package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.LoginUserDTO;

public class LoginUserDTOMapper {
    public LoginUserDTO mapToDTO(Long chatId) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setChatId(chatId);
        return loginUserDTO;
    }
}
