package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramResponse;

public interface TelegramKeyboardService {
    TelegramResponse createTelegramResponseWithChatMainKeyboard(Long chatId,
                                                                String text);
    TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(
            Long chatId, String text);

}
