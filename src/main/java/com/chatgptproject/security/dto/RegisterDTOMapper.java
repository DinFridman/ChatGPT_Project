package com.chatgptproject.security.dto;

public class RegisterDTOMapper {
    public RegisterDTO mapToDTO(String username, String password) {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(username);
        registerDTO.setPassword(password);
        return registerDTO;
    }
}
