package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramRequestServiceImpl implements TelegramRequestService{
    private final com.example.chatgptproject.service.openAIService.OpenAIRequestHandlerImpl OpenAIRequestHandlerImpl;
    private final MessagesServiceImpl messagesServiceImpl;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final EmailServiceImpl emailService;

    @Override
    @Transactional
    public TelegramMessageResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        Long chatId = chatMessageDTO.getChatId();
        addChatMessageToConversation(chatMessageDTO);

        ConversationDTO conversationDTO = getCurrentConversation(chatMessageDTO);
       /* if(isShareConversationRequest(chatMessageDTO)) { //TODO: should be an endpoint in the API
            handleShareConversationRequest(chatMessageDTO,);
            return createTelegramResponse(chatId, Constants.SHARE_CONVERSATION_BY_EMAIL_RESPONSE);
        }*/

        OpenAIPromptDTO openAIPromptDTO = generateAnswerByRequestHandler(conversationDTO);

        log.info("----------AI_Answer  : " + openAIPromptDTO  + "----------");

        ChatMessageDTO openAIChatMessageDTO =
                createChatMessageDTOForOpenAIResponse(chatMessageDTO,openAIPromptDTO);
        addChatMessageToConversation(openAIChatMessageDTO);

        return createTelegramResponse(chatId,openAIPromptDTO.content());
    }

    @Override
    public void addChatMessageToConversation(ChatMessageDTO chatMessageDTO) {
        messagesServiceImpl.addChatMessage(chatMessageDTO);
    }

    private Long getUserIdFromChatMessage(ChatMessageDTO chatMessageDTO) {
        return messagesServiceImpl.getAppUserFromChatMessageDTO(chatMessageDTO).getUserId();
    }

    @Override
    public void handleShareConversationRequest(
            ChatMessageDTO chatMessageDTO, String recipient) {
        ConversationDTO conversationDTO = getCurrentConversation(chatMessageDTO);
        emailService.handleShareConversationRequest(
                conversationDTO, recipient);
    }

    @Override
    public boolean isShareConversationRequest(ChatMessageDTO chatMessageDTO) {
        return chatMessageDTO.getMessage()
                .startsWith(Constants.SHARE_CONVERSATION_BY_EMAIL_REQUEST);
    }

    @Override
    public ConversationDTO getCurrentConversation(ChatMessageDTO chatMessageDTO) {
        Long userId = getUserIdFromChatMessage(chatMessageDTO);
        return messagesServiceImpl.getConversationByUserId(userId);
    }

    @Override
    public OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        return OpenAIRequestHandlerImpl.generateAnswer(conversationDTO);
    }

    @Override
    public ChatMessageDTO createChatMessageDTOForOpenAIResponse(ChatMessageDTO chatMessageDTO,
                                                      OpenAIPromptDTO openAIPromptDTO) {
        return createChatMessageDTO(
                chatMessageDTO,
                openAIPromptDTO.role(),
                openAIPromptDTO.content());
    }

    @Override
    public ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                               String role, String content) {

        return ChatMessageDTO.builder()
                .chatId(chatMessageDTO.getChatId())
                .message(content)
                .role(role)
                .updateId(chatMessageDTO.getUpdateId())
                .username(chatMessageDTO.getUsername())
                .build();
    }

    @Override
    public TelegramMessageResponseDTO createTelegramResponse(Long chatId,
                                                             String content) {
        return telegramResponseDTOMapper.mapToDTO(chatId, content);
    }
}