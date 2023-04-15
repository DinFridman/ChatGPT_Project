package com.example.chatgptproject.service;

import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.service.openAIService.OpenAIRequestHandlerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramRequestServiceImplTest {
    @InjectMocks private TelegramRequestServiceImpl underTest;
    @Mock private OpenAIRequestHandlerImpl generateAnswerService;
    @Mock private MessagesServiceImpl messagesServiceImpl;
    @Captor private ArgumentCaptor<ChatMessageEntity> captor;


    @Test
    void generateAnswer() throws JSONException, JsonProcessingException {
       /* //given
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .updateId(1000)
                .chatId(10000)
                .message("Hello")
                .userId(2)
                .role(Roles.USER.toString())
                .build();
        OpenAIPromptDTO prompt = OpenAIPromptDTO.builder()
                .role(Roles.USER.toString())
                .content("Hello")
                .build();
        ArrayList<OpenAIPromptDTO> messages = new ArrayList<>();
        messages.add(prompt);
        ConversationDTO conversationDTO =
                ConversationDTO.builder()
                        .conversation(messages)
                                .build();
        //when
        underTest.generateAnswer(chatMessage);

        //then
        verify(generateAnswerService).generateAnswer(conversationDTO);*/

    }
}