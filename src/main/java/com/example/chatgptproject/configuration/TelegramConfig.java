package com.example.chatgptproject.configuration;

import com.orgyflame.springtelegrambotapi.bot.container.BotApiMappingContainer;
import com.orgyflame.springtelegrambotapi.bot.container.DefaultBotApiMappingContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public BotApiMappingContainer botApiMappingContainer(){
        return new DefaultBotApiMappingContainer();
    }
}
