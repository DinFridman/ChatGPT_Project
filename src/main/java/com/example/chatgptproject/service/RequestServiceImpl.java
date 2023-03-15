package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.ChatAnswerDTOMapper;
import com.example.chatgptproject.utils.enums.ChatRoles;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService{
    private static final Logger logger = LogManager.getLogger("requestService-logger");
    private final OpenAIServiceImpl openAIServiceImpl;
    private final MessagesServiceImpl messagesServiceImpl;
    private final ChatAnswerDTOMapper chatAnswerDTOMapper;

    @Override
    public ChatAnswerDTO generateAnswer(ChatMessageDTO chatMessageDTO)
            throws JsonProcessingException, JSONException {
        ConversationDTO conversationDTO = messagesServiceImpl.addChatMessageAndGetConversation(
                chatMessageDTO);

        OpenAIPromptDTO openAIPromptDTO = openAIServiceImpl
                .generateAnswer(conversationDTO);

        logger.info("----------AI_Answer : " + openAIPromptDTO  + "----------");

        messagesServiceImpl.addChatMessage(
                ChatMessageDTO.builder()
                        .chatId(chatMessageDTO.getChatId())
                        .message(openAIPromptDTO.getContent())
                        .role(ChatRoles.ASSISTANCE.toString())
                        .updateId(chatMessageDTO.getUpdateId())
                        .userId(chatMessageDTO.getUserId())
                        .build());

        return chatAnswerDTOMapper.mapToChatAnswerDTO(
                chatMessageDTO.getChatId(),
                openAIPromptDTO.getContent());
    }

}