package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.dto.OpenAIMessageDTO;
import com.example.chatgptproject.dto.mapper.ChatAnswerDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramRequestDTOMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

@Service
public class RequestService {
    private static final Logger logger = LogManager.getLogger("RequestService-logger");
    private TelegramRequestDTOMapper telegramRequestDTOMapper;
    private GenerateAnswerService generateAnswerService;
    private ChatAnswerDTOMapper chatAnswerDTOMapper;

    public ChatAnswerDTO generateAnswer(Update update) throws JsonProcessingException, JSONException {
        OpenAIMessageDTO openAIMessageDTO = generateAnswerService
                .generateAnswer(telegramRequestDTOMapper
                        .mapToDTO(update));

        ChatAnswerDTO chatAnswerDTO = chatAnswerDTOMapper.mapToChatAnswerDTO(
                update.getMessage().getChatId().toString(),
                openAIMessageDTO.getContent());

        logger.info("----------AI_Answer : " + chatAnswerDTO.getMessage()  + "----------");
        return chatAnswerDTO;

    }

}