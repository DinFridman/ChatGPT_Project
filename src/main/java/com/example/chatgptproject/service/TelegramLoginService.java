package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramResponse;

public interface TelegramLoginService {
    TelegramResponse handleLoginState(Long chatId, String message);
    TelegramResponse handleUsernameInput(Long chatId, String username);
    TelegramResponse loginUserAndGetTelegramResponse(Long chatId);
}
