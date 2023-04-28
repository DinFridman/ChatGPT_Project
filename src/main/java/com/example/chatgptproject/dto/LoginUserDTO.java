package com.example.chatgptproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class LoginUserDTO implements Serializable {
    private String username;
    private String password;
    private Long chatId;
    private LocalDate loggedInDate;
    private Boolean isLoggedIn = false;
    private Boolean isRegisterRequest = false;
    private Boolean isLoginRequest = false;
    private Boolean isEmailConversationRequest = false;
}
