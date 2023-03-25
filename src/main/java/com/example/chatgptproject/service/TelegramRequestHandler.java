package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramRequestHandler {
    private final OpenAIRequestHandler OpenAIRequestHandler;
    private final MessagesService messagesService;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;

    @Transactional
    public TelegramResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        ConversationDTO conversationDTO = updateAndGetCurrentConversation(chatMessageDTO);

        OpenAIPromptDTO openAIPromptDTO = generateAnswerByRequestHandler(conversationDTO);

        log.info("----------AI_Answer : " + openAIPromptDTO  + "----------");

        addAnswerToConversation(chatMessageDTO,openAIPromptDTO);

        return  createTelegramResponse(chatMessageDTO,openAIPromptDTO);
    }

    public ConversationDTO updateAndGetCurrentConversation(ChatMessageDTO chatMessageDTO) {
        return messagesService.addChatMessageAndGetConversation(chatMessageDTO);
    }

    public OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        return OpenAIRequestHandler.generateAnswer(conversationDTO);
    }

    public void addAnswerToConversation(ChatMessageDTO chatMessageDTO,
                                        OpenAIPromptDTO openAIPromptDTO) {

        messagesService.addChatMessage(
                createChatMessageDTO(
                        chatMessageDTO,openAIPromptDTO));
    }

    public ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                               OpenAIPromptDTO openAIPromptDTO) {

        return ChatMessageDTO.builder()
                .chatId(chatMessageDTO.getChatId())
                .message(openAIPromptDTO.content())
                .role(Roles.ASSISTANCE.toString())
                .updateId(chatMessageDTO.getUpdateId())
                .userId(chatMessageDTO.getUserId())
                .build();
    }

    public TelegramResponseDTO createTelegramResponse(ChatMessageDTO chatMessageDTO,
                                                      OpenAIPromptDTO openAIPromptDTO) {
        return telegramResponseDTOMapper.mapToDTO(
                chatMessageDTO.getChatId(),
                openAIPromptDTO.content());
    }
}