package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramResponse;

public interface TelegramLoginService {
    TelegramResponse handleLoginState(Long chatId, String message);
}
