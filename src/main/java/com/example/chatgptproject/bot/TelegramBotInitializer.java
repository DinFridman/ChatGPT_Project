package com.example.chatgptproject.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBotInitializer implements InitializingBean {

    private final TelegramBotsApi telegramBotsApi;
    private final TelegramBot telegramBot;

    @Override
    public void afterPropertiesSet() {
        try {
                telegramBotsApi.registerBot(telegramBot);
            } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }

}
