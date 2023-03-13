package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.ChatRepository;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessagesServiceTest {
    @InjectMocks private MessagesService underTest;
    @Mock private ChatRepository mockChatRepository;
    @Mock private ChatMessageMapper mockChatMessageMapper;
    @Mock private OpenAIPromptDTOMapper mockOpenAIPromptDTOMapper;
    @Captor private ArgumentCaptor<ChatMessageEntity> captor;


    @Test
    void shouldAddChatMessage() {
        //given
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .updateId(1000)
                .chatId(10000)
                .message("Hello")
                .userId(2)
                .role(Roles.USER.toString())
                .build();
        ChatMessageEntity chatMessageEntity =
                mockChatMessageMapper.mapToEntity(chatMessage);

        //when
        underTest.addChatMessage(chatMessage);

        //then
        verify(mockChatRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(chatMessageEntity);

    }

    @Test
    void shouldAddChatMessageAndGetConversation() {
        //given
        long chatId = 1;
        ChatMessageDTO chatMessageDTO =
                ChatMessageDTO.builder()
                        .updateId(1000)
                        .chatId(chatId)
                        .message("Hello")
                        .userId(10000)
                        .role(Roles.USER.toString())
                        .build();
        ChatMessageEntity chatMessageEntity =
                mockChatMessageMapper.mapToEntity(chatMessageDTO);

        //when
        underTest.addChatMessage(chatMessageDTO);
        underTest.getConversationById(chatId);

        //then
        verify(mockChatRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(chatMessageEntity);
        verify(mockChatRepository).findMessagesByChatId(chatId);
    }

    @Test
    void shouldGetConversationById() {
        //given
        long chatId = 1000;

        //when
        underTest.getConversationById(chatId);

        //then
        verify(mockChatRepository).findMessagesByChatId(chatId);
    }

}