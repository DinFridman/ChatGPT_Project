package com.example.chatgptproject.configuration;

import com.example.chatgptproject.dto.mapper.OpenAIRequestDTOMapper;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("applicationConfig")
public class ApplicationConfig {

    @Bean
    public ChatMessageDTOMapper telegramRequestDTOMapper() {return new ChatMessageDTOMapper();}

    @Bean
    public ObjectMapper objectMapper() {return new ObjectMapper();}

    @Bean
    public TelegramResponseDTOMapper telegramResponseDTOMapper() {return new TelegramResponseDTOMapper();}

    @Bean
    public ChatMessageMapper chatMessageMapper() {return new ChatMessageMapper();}

    @Bean
    public OpenAIPromptDTOMapper openAIPromptDTOMapper() {return new OpenAIPromptDTOMapper();}

    @Bean
    public OpenAIRequestDTOMapper chatCompletionDTOMapper() {return new OpenAIRequestDTOMapper();}

    @Bean
    public ModelMapper modelMapper() {return new ModelMapper();}

}
