package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.ChatAnswerDTOMapper;
import com.example.chatgptproject.utils.enums.Roles;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private static final Logger logger = LogManager.getLogger("RequestService-logger");
    private final GenerateAnswerService generateAnswerService;
    private final MessagesService messagesService;
    private final ChatAnswerDTOMapper chatAnswerDTOMapper;

    public RequestService(GenerateAnswerService generateAnswerService,
                          MessagesService messagesService,
                          ChatAnswerDTOMapper chatAnswerDTOMapper) {
        this.generateAnswerService = generateAnswerService;
        this.messagesService = messagesService;
        this.chatAnswerDTOMapper = chatAnswerDTOMapper;
    }

    public ChatAnswerDTO generateAnswer(ChatMessageDTO chatMessageDTO) throws JsonProcessingException, JSONException {
        ConversationDTO conversationDTO = messagesService.addChatMessageAndGetConversation(
                chatMessageDTO);

        OpenAIPromptDTO openAIPromptDTO = generateAnswerService
                .generateAnswer(conversationDTO);

        logger.info("----------AI_Answer : " + openAIPromptDTO  + "----------");

        messagesService.addChatMessage(
                ChatMessageDTO.builder()
                        .chatId(chatMessageDTO.getChatId())
                        .message(openAIPromptDTO.getContent())
                        .role(Roles.ASSISTANCE.toString())
                        .updateId(chatMessageDTO.getUpdateId())
                        .userId(chatMessageDTO.getUserId())
                        .build());

        return chatAnswerDTOMapper.mapToChatAnswerDTO(
                chatMessageDTO.getChatId(),
                openAIPromptDTO.getContent());
    }

}