package com.example.chatgptproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class LoginUserDTO {
    private String username = null;
    private String password = null;
    private String email = null;
    private String phoneNumber = null;
    private LocalDate loggedInDate;
    private Boolean isLoggedIn = false;
    private Boolean isRegisterRequest = false;
    private Boolean isLoginRequest = false;
    private Boolean isEmailConversationRequest = false;
}
