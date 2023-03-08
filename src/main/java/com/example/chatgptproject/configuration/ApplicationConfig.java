package com.example.chatgptproject.configuration;

import com.example.chatgptproject.dto.mapper.ChatAnswerDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramRequestDTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TelegramRequestDTOMapper telegramRequestDTOMapper() {return new TelegramRequestDTOMapper();}

    @Bean
    public ChatAnswerDTOMapper chatAnswerDTOMapper() {return new ChatAnswerDTOMapper();}




}
