package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;

public interface TelegramLoginService {
    TelegramResponse handleLoginState(Long chatId, String message);
}
