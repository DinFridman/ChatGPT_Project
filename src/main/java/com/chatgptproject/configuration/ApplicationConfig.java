package com.chatgptproject.configuration;

import com.chatgptproject.dto.EmailDetailsDTO;
import com.chatgptproject.dto.mapper.*;
import com.chatgptproject.model.mapper.ChatMessageMapper;
import com.chatgptproject.security.dto.RegisterDTOMapper;
import com.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@EnableCaching
@Configuration("applicationConfig")
public class ApplicationConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .serializers(
                                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")))
                        .serializationInclusion(JsonInclude.Include.NON_NULL);
        return new MappingJackson2HttpMessageConverter(builder.build());
    }

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
