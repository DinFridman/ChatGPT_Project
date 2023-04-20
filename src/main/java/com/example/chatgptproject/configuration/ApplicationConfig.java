package com.example.chatgptproject.configuration;

import com.example.chatgptproject.dto.mapper.*;
import com.example.chatgptproject.dto.EmailDetailsDTO;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("applicationConfig")
public class ApplicationConfig {
    @Value("${redis.host}")
    private String redisHostName;
    @Value("${redis.port}")
    private int redisPort;

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

    @Bean
    public RegisterDTOMapper registerDTOMapper() {return new RegisterDTOMapper();}

    @Bean
    public LoginUserDTOMapper loginUserDTOMapper() {return new LoginUserDTOMapper();}

    @Bean
    public EmailDetailsDTO emailDetails() {return new EmailDetailsDTO();}

    @Bean
    public EmailDetailsDTOMapper emailDetailsDTOMapper() {return new EmailDetailsDTOMapper();}

    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}
}
