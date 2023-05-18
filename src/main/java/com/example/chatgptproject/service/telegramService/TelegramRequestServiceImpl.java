package com.example.chatgptproject.service.telegramService;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.service.EmailServiceImpl;
import com.example.chatgptproject.service.MessagesServiceImpl;
import com.example.chatgptproject.service.openAIService.OpenAIRequestHandlerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private void addChatMessageToConversation(ChatMessageDTO chatMessageDTO) {
        messagesServiceImpl.addChatMessage(chatMessageDTO);
    }

    @Override
    public void handleShareConversationRequest(
            ChatMessageDTO chatMessageDTO, String recipient) {
        ConversationDTO conversationDTO = getCurrentConversation(chatMessageDTO);
        emailService.handleShareConversationRequest(
                conversationDTO, recipient);
    }

    private ConversationDTO getCurrentConversation(ChatMessageDTO chatMessageDTO) {
        Long userId = getUserIdFromChatMessage(chatMessageDTO);
        return messagesServiceImpl.getConversationByUserId(userId);
    }

    private Long getUserIdFromChatMessage(ChatMessageDTO chatMessageDTO) {
        return messagesServiceImpl.getAppUserFromChatMessageDTO(chatMessageDTO).getUserId();
    }

    private OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        return OpenAIRequestHandlerImpl.generateAnswer(conversationDTO);
    }

    private ChatMessageDTO createChatMessageDTOForOpenAIResponse(ChatMessageDTO chatMessageDTO,
                                                      OpenAIPromptDTO openAIPromptDTO) {
        return createChatMessageDTO(
                chatMessageDTO,
                openAIPromptDTO.role(),
                openAIPromptDTO.content());
    }

    private ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                               String role, String content) {

        return ChatMessageDTO.builder()
                .chatId(chatMessageDTO.getChatId())
                .message(content)
                .role(role)
                .updateId(chatMessageDTO.getUpdateId())
                .username(chatMessageDTO.getUsername())
                .build();
    }

    private TelegramMessageResponseDTO createTelegramResponse(Long chatId,
                                                             String content) {
        return telegramResponseDTOMapper.mapToDTO(chatId, content);
    }
}