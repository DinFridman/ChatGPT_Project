package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;

public interface TelegramKeyboardService {
    TelegramResponse createTelegramResponseWithChatMainKeyboard(Long chatId,
                                                                String text);
    TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(
            Long chatId, String text);

}
