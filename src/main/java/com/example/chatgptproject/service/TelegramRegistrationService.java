package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramResponse;

public interface TelegramRegistrationService {
    TelegramResponse handleRegisterState(Long chatId, String message);
    TelegramResponse handleUsernameInput(Long chatId, String username);
    boolean checkIfUserExists(String username);
}
