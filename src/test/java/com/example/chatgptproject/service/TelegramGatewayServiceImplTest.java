package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramKeyBoardMessageDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.utils.TelegramRequestTypeResolver;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;
import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.*;
import static com.example.chatgptproject.utils.enums.TelegramRequestType.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TelegramGatewayServiceImplTest {
    @Mock private TelegramRequestTypeResolver telegramRequestTypeResolver;
    @Mock private TelegramLoginServiceImpl telegramLoginServiceImpl;
    @Mock private TelegramResponseDTOMapper telegramResponseDTOMapper;
    @Mock private TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    @Mock private TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    @Mock private TelegramRegistrationServiceImpl telegramRegistrationServiceImpl;
    @Mock private ConversationSenderServiceImpl conversationSenderServiceImpl;
    @Mock private TelegramRequestServiceImpl telegramRequestServiceImpl;
    @InjectMocks private TelegramGatewayServiceImpl underTest;
    private Update update;
    private Message message;
    private Long chatId;

    @BeforeEach
    void setUp() {
        update = mock(Update.class);
        message = mock(Message.class);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(message.getChatId()).thenReturn(1234L);
        chatId = 1234L;
    }

    @Test
    void shouldHandleInvalidTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO =
                TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_INVALID_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn(null);
        Mockito.when(telegramRequestTypeResolver.resolve(null,chatId))
                .thenReturn(EMPTY_OR_NULL_MESSAGE);
        Mockito.when(telegramResponseDTOMapper
                .mapToDTO(chatId,TELEGRAM_INVALID_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleNoSessionTelegramRequest() throws IOException, InterruptedException {

        TelegramKeyBoardMessageDTO expectedTelegramResponseDTO =
                TelegramKeyBoardMessageDTO.builder()
                        .text(TELEGRAM_LOGIN_OR_REGISTER_REQUEST_MESSAGE)
                        .chatId(chatId)
                        .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve("test",chatId))
                .thenReturn(NO_SESSION);
        Mockito.when(telegramKeyboardServiceImpl
                        .createTelegramResponseWithLoginRegisterKeyboard(
                                chatId,TELEGRAM_LOGIN_OR_REGISTER_REQUEST_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleNotLoggedInTelegramRequest() throws IOException, InterruptedException {
        TelegramResponse expectedTelegramResponseDTO = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_LOGIN_OR_REGISTER_REQUEST_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(NO_SESSION);
        Mockito.when(telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                chatId,TELEGRAM_LOGIN_OR_REGISTER_REQUEST_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleLoginButtonPressedTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_ENTER_USERNAME_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGIN_BUTTON_PRESSED);
        Mockito.when(telegramUserStateServiceImpl.startLoginState(chatId))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleRegisterButtonPressedTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_ENTER_USERNAME_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(REGISTER_BUTTON_PRESSED);
        Mockito.when(telegramUserStateServiceImpl.startRegisterState(chatId))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleLogoutButtonPressedTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_LOGOUT_SUCCESSFULLY_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGOUT_BUTTON_PRESSED);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,TELEGRAM_LOGOUT_SUCCESSFULLY_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleRegisterUsernameTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(REGISTER_REQUEST);
        Mockito.when(telegramRegistrationServiceImpl.handleRegisterState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnUsernameExistsOnRegisterTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_EXISTS_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(REGISTER_REQUEST);
        Mockito.when(telegramRegistrationServiceImpl.handleRegisterState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleRegisterPasswordEnterTelegramRequest() throws IOException, InterruptedException {
        TelegramKeyBoardMessageDTO expectedTelegramResponseDTO =
                TelegramKeyBoardMessageDTO.builder()
                        .text(TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE)
                        .chatId(chatId)
                        .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(REGISTER_REQUEST);
        Mockito.when(telegramRegistrationServiceImpl.handleRegisterState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnIncorrectPasswordOnRegisterTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_INCORRECT_PASSWORD_ENTERED)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(REGISTER_REQUEST);
        Mockito.when(telegramRegistrationServiceImpl
                        .handleRegisterState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleLoginUsernameTelegramRequest() throws IOException, InterruptedException {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE
                + "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGIN_REQUEST);
        Mockito.when(telegramLoginServiceImpl.handleLoginState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnUsernameDoesNotExistsOnLoginTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGIN_REQUEST);
        Mockito.when(telegramLoginServiceImpl.handleLoginState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleLoginPasswordEnterTelegramRequest() throws IOException, InterruptedException {
        TelegramKeyBoardMessageDTO expectedTelegramResponseDTO =
                TelegramKeyBoardMessageDTO.builder()
                        .text(TELEGRAM_USER_LOGGED_IN_SUCCESSFULLY_RESPONSE)
                        .chatId(chatId)
                        .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGIN_REQUEST);
        Mockito.when(telegramLoginServiceImpl.handleLoginState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnIncorrectPasswordOnLoginTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_INCORRECT_PASSWORD_ENTERED)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(LOGIN_REQUEST);
        Mockito.when(telegramLoginServiceImpl
                        .handleLoginState(chatId,message.getText()))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleEmailConversationButtonPressedTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_ENTER_EMAIL_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(SEND_CONVERSATION_BUTTON_PRESSED);
        Mockito.when(telegramUserStateServiceImpl.startSendConversationState(chatId))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleSendConversationTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY)
                .chatId(chatId)
                .build();

        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .username("test")
                .updateId(0L)
                .message("test")
                .role(Roles.USER.toString())
                .build();

        Mockito.when(telegramUserStateServiceImpl.getUsernameFromUserSessionByChatId(chatId))
                        .thenReturn("test");
        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(SEND_CONVERSATION_REQUEST);
        Mockito.when(conversationSenderServiceImpl.handleSendConversationRequest(chatMessageDTO))
                .thenReturn(CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY);
        Mockito.when(telegramKeyboardServiceImpl
                        .createTelegramResponseWithChatMainKeyboard(
                                chatId,CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldGenerateAnswerTelegramRequest()
            throws IOException, InterruptedException {

        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text("OpenAiPrompt")
                .chatId(chatId)
                .build();

        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .username("test")
                .updateId(0L)
                .message("test")
                .role(Roles.USER.toString())
                .build();

        Mockito.when(telegramUserStateServiceImpl.getUsernameFromUserSessionByChatId(chatId))
                .thenReturn("test");
        Mockito.when(message.getText()).thenReturn("test");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(telegramRequestTypeResolver.resolve(message.getText(),chatId))
                .thenReturn(GENERATE_ANSWER);
        Mockito.when(telegramRequestServiceImpl.handleTelegramRequest(chatMessageDTO))
                .thenReturn(expectedTelegramResponseDTO);
        Mockito.when(telegramKeyboardServiceImpl
                        .createTelegramResponseWithChatMainKeyboard(
                                chatId,"OpenAiPrompt"))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleTelegramRequest(update);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }


}