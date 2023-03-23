package com.example.chatgptproject.configuration;

import com.example.chatgptproject.dto.mapper.ChatAnswerDTOMapper;
import com.example.chatgptproject.dto.mapper.ChatCompletionDTOMapper;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("applicationConfig")
public class ApplicationConfig {

    @Bean
    public ChatMessageDTOMapper telegramRequestDTOMapper() {return new ChatMessageDTOMapper();}

    @Bean
    public ChatAnswerDTOMapper chatAnswerDTOMapper() {return new ChatAnswerDTOMapper();}

    @Bean
    public ChatMessageMapper chatMessageMapper() {return new ChatMessageMapper();}

    @Bean
    public OpenAIPromptDTOMapper openAIPromptDTOMapper() {return new OpenAIPromptDTOMapper();}

    @Bean
    public ChatCompletionDTOMapper chatCompletionDTOMapper() {return new ChatCompletionDTOMapper();}

    @Bean
    public ModelMapper modelMapper() {return new ModelMapper();}

}
