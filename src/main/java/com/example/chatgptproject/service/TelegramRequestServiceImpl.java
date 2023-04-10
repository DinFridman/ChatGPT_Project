package com.example.chatgptproject.service;
import com.example.chatgptproject.dto.TelegramResponseDTO;
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
    private final OpenAIRequestHandlerImpl OpenAIRequestHandlerImpl;
    private final MessagesServiceImpl messagesServiceImpl;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final EmailServiceImpl emailService;

    @Override
    @Transactional
    public TelegramResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        addUserMessageToConversation(chatMessageDTO);
        ConversationDTO conversationDTO = getCurrentConversation(chatMessageDTO.getChatId());

        if(isShareConversationRequest(chatMessageDTO))
            return handleShareConversationRequestAndReturnResponse(
                    conversationDTO,chatMessageDTO);

        OpenAIPromptDTO openAIPromptDTO = generateAnswerByRequestHandler(conversationDTO);

        log.info("----------AI_Answer  : " + openAIPromptDTO  + "----------");

        addOpenAIMessageToConversation(chatMessageDTO,openAIPromptDTO);

        return  createTelegramResponse(chatMessageDTO.getChatId(),openAIPromptDTO.content());
    }

    @Override
    public void addUserMessageToConversation(ChatMessageDTO chatMessageDTO) {
        messagesServiceImpl.addChatMessage(chatMessageDTO);
    }

    @Override
    public TelegramResponseDTO handleShareConversationRequestAndReturnResponse(
            ConversationDTO conversationDTO, ChatMessageDTO chatMessageDTO) {

        emailService.handleShareConversationRequest(
                conversationDTO, chatMessageDTO.getMessage());
        String answer = Constants.SHARE_CONVERSATION_BY_EMAIL_RESPONSE;
        return createTelegramResponse(chatMessageDTO.getChatId(),answer);
    }

    @Override
    public boolean isShareConversationRequest(ChatMessageDTO chatMessageDTO) {
        return chatMessageDTO.getMessage()
                .startsWith(Constants.SHARE_CONVERSATION_BY_EMAIL_REQUEST);
    }

    @Override
    @Cacheable("conversation")
    public ConversationDTO getCurrentConversation(Long chatId) {
        return messagesServiceImpl.getConversationById(chatId);
    }

    @Override
    public OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        return OpenAIRequestHandlerImpl.generateAnswer(conversationDTO);
    }

    @Override
    public void addOpenAIMessageToConversation(ChatMessageDTO chatMessageDTO,
                                               OpenAIPromptDTO openAIPromptDTO) {

        messagesServiceImpl.addChatMessage(
                createChatMessageDTO(
                        chatMessageDTO,openAIPromptDTO.role(),openAIPromptDTO.content()));
    }

    @Override
    public ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                               String role, String content) {

        return ChatMessageDTO.builder()
                .chatId(chatMessageDTO.getChatId())
                .message(content)
                .role(role)
                .updateId(chatMessageDTO.getUpdateId())
                .userId(chatMessageDTO.getUserId())
                .build();
    }

    @Override
    public TelegramResponseDTO createTelegramResponse(Long chatId,
                                                      String content) {
        return telegramResponseDTOMapper.mapToDTO(chatId, content);
    }
}