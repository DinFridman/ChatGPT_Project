package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.*;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.service.openAIService.OpenAIRequestHandlerImpl;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramRequestServiceImplTest {
    @InjectMocks private TelegramRequestServiceImpl underTest;
    @Mock private OpenAIRequestHandlerImpl openAIRequestHandler;
    @Mock private MessagesServiceImpl messagesServiceImpl;
    @Mock private TelegramResponseDTOMapper telegramResponseDTOMapper;
    private ChatMessageDTO chatMessageDTO;
    private ConversationDTO conversationDTO;
    private OpenAIPromptDTO openAIPromptDTO;
    private TelegramMessageResponseDTO expectedTelegramResponse;
    private AppUserEntity appUser;

    @BeforeEach
    public void setup() {
        Long chatId = 12345L;
        chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .message("Hello")
                .username("Test")
                .updateId(54321L)
                .role(Roles.USER.toString())
                .build();

        ArrayList<OpenAIPromptDTO> messagesArray = new ArrayList<>();
        messagesArray.add(
                new OpenAIPromptDTOMapper().mapToOpenAIPromptDTO(
                        chatMessageDTO.getRole(),
                        chatMessageDTO.getMessage()));

        conversationDTO = ConversationDTO.builder()
                .conversation(messagesArray)
                .build();

        openAIPromptDTO = new OpenAIPromptDTOMapper()
                .mapToOpenAIPromptDTO(Roles.ASSISTANCE.toString(), "Hi there!");

        expectedTelegramResponse = TelegramMessageResponseDTO.builder()
                .text(openAIPromptDTO.content())
                .chatId(chatId)
                .build();

        appUser = new AppUserEntity();
        appUser.setUserId(1L);
        appUser.setUsername("Test");
        appUser.setPassword("TestPass");
        appUser.setLoggedInDate(LocalDateTime.now());
    }

    @Test
    void shouldHandleTelegramResponse() throws IOException, InterruptedException {
        when(messagesServiceImpl.getAppUserFromChatMessageDTO(chatMessageDTO)).thenReturn(appUser);
        when(messagesServiceImpl.getConversationByUserId(appUser.getUserId())).thenReturn(conversationDTO);
        when(openAIRequestHandler.generateAnswer(conversationDTO)).thenReturn(openAIPromptDTO);
        when(telegramResponseDTOMapper.mapToDTO(
                chatMessageDTO.getChatId(),
                "Hi there!"))
                .thenReturn(expectedTelegramResponse);

        TelegramMessageResponseDTO returnedTelegramResponse =
                underTest.handleTelegramRequest(chatMessageDTO);

        assertThat(returnedTelegramResponse).isEqualTo(expectedTelegramResponse);

    }

}