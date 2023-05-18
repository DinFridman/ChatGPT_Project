package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.dto.TelegramResponse;

public interface TelegramRegistrationService {
    TelegramResponse handleRegisterState(Long chatId, String message);
}
