package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.dto.TelegramMessageResponseDTO;
import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.chatgptproject.service.ConversationSenderServiceImpl;
import com.chatgptproject.utils.TelegramRequestTypeResolver;
import com.chatgptproject.utils.enums.TelegramRequestType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;

import static com.chatgptproject.utils.constants.TelegramResponseConstants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayServiceImpl implements TelegramGatewayService{
    private final TelegramRegistrationServiceImpl telegramRegistrationServiceImpl;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    private final TelegramLoginServiceImpl telegramLoginServiceImpl;
    private final ConversationSenderServiceImpl conversationSenderServiceImpl;
    private final TelegramRequestTypeResolver telegramRequestTypeResolver;

    @Override
    public TelegramResponse handleTelegramRequest(@NotNull Update update)
            throws IOException, InterruptedException {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        TelegramRequestType telegramRequestType =
                telegramRequestTypeResolver.resolve(message, chatId);

        return switch (telegramRequestType) {
            case EMPTY_OR_NULL_MESSAGE -> handleInvalidRequest(chatId);
            case NO_SESSION, NOT_LOGGED_IN -> handleStartingChatState(chatId);
            case LOGIN_BUTTON_PRESSED -> startLoginState(chatId);
            case REGISTER_BUTTON_PRESSED -> startRegisterState(chatId);
            case LOGOUT_BUTTON_PRESSED -> handleLogoutRequest(chatId);
            case REGISTER_REQUEST -> handleRegisterState(chatId, message);
            case LOGIN_REQUEST -> handleLoginState(chatId, message);
            case SEND_CONVERSATION_BUTTON_PRESSED -> startSendConversationState(chatId);
            case SEND_CONVERSATION_REQUEST -> handleSendConversationRequest(update);
            default -> handleGenerateAnswerState(update);
        };
    }

    private TelegramResponse handleInvalidRequest(Long chatId) {
        return getTelegramResponseDTO(chatId, TELEGRAM_INVALID_MESSAGE);
    }

    private TelegramResponse startLoginState(Long chatId) {
        return telegramUserStateServiceImpl.startLoginState(chatId);
    }

    private TelegramResponse startRegisterState(Long chatId) {
        return telegramUserStateServiceImpl.startRegisterState(chatId);
    }

    private TelegramResponse startSendConversationState(Long chatId) {
        return telegramUserStateServiceImpl.startSendConversationState(chatId);
    }

    private TelegramResponse handleSendConversationRequest(Update update) {
        Long chatId = getChatIdFromUpdate(update);
        ChatMessageDTO chatMessageDTO = createChatMessageDTO(update);
        String emailResponse =
                conversationSenderServiceImpl.handleSendConversationRequest(chatMessageDTO);
        return createTelegramMainChatKeyboardResponse(chatId, emailResponse);
    }

    private TelegramResponse handleLogoutRequest(Long chatId) {
        telegramUserStateServiceImpl.handleLogoutRequest(chatId);

        return getTelegramResponseDTO(chatId, TELEGRAM_LOGOUT_SUCCESSFULLY_MESSAGE);
    }

    private TelegramResponse handleStartingChatState(Long chatId) {
        telegramUserStateServiceImpl.handleStartingChatState(chatId);

        return createTelegramLoginRegisterKeyboardResponse(chatId,
                TELEGRAM_LOGIN_OR_REGISTER_REQUEST_MESSAGE);
    }

    private TelegramResponse handleGenerateAnswerState(Update update)
            throws IOException, InterruptedException {

        ChatMessageDTO chatMessageDTO = createChatMessageDTO(update);

        TelegramMessageResponseDTO openAIResponse = telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);

        String responseMessage = ExtractMessageFromResponse(openAIResponse);

        return createTelegramMainChatKeyboardResponse(
                chatMessageDTO.getChatId(),responseMessage);
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }

    private TelegramResponse createTelegramMainChatKeyboardResponse(Long chatId,
                                                                    String chatMessage) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithChatMainKeyboard(
                chatId, chatMessage);
    }

    private TelegramResponse handleRegisterState(Long chatId, String message) {
        return telegramRegistrationServiceImpl.handleRegisterState(chatId,message);
    }

    private TelegramResponse handleLoginState(Long chatId, String message) {
        return telegramLoginServiceImpl.handleLoginState(chatId,message);
    }

    private ChatMessageDTO createChatMessageDTO(Update update) {
        Long chatId = getChatIdFromUpdate(update);
        String username = telegramUserStateServiceImpl.getUsernameFromUserSessionByChatId(chatId);
        return new ChatMessageDTOMapper().mapToDTO(update, username);
    }

    private String ExtractMessageFromResponse(TelegramMessageResponseDTO telegramMessageResponseDTO) {
        return telegramMessageResponseDTO.getText();
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    private TelegramResponse getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }
}
