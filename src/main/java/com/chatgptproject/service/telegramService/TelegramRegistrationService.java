package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;

public interface TelegramRegistrationService {
    TelegramResponse handleRegisterState(Long chatId, String message);
}
