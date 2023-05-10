package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY;
import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.TELEGRAM_ENTER_EMAIL_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConversationSenderServiceImplTest {
    @Mock TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    @Mock TelegramRequestServiceImpl telegramRequestService;
    @InjectMocks ConversationSenderServiceImpl underTest;
    private Long chatId;
    private String expectedResponse;
    private ChatMessageDTO chatMessageDTO;

    @BeforeEach
    void setUp() {
        chatId = 1L;
        chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .username("test")
                .updateId(0L)
                .message("test")
                .role(Roles.USER.toString())
                .build();
    }

    @Test
    void shouldReturnSentConversationSuccessfullyResponse() {
        expectedResponse = CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY;
        Mockito.when(telegramUserStateServiceImpl.checkIfEmailConversationStateOn(chatId))
                .thenReturn(true);

        String actualResponse = underTest.handleSendConversationRequest(chatMessageDTO);

        assertEquals(expectedResponse,actualResponse);

    }

    @Test
    void shouldReturnEnterEmailResponse() {
        expectedResponse = TELEGRAM_ENTER_EMAIL_MESSAGE;
        Mockito.when(telegramUserStateServiceImpl.checkIfEmailConversationStateOn(chatId))
                .thenReturn(false);

        String actualResponse = underTest.handleSendConversationRequest(chatMessageDTO);

        assertEquals(expectedResponse,actualResponse);

    }
}