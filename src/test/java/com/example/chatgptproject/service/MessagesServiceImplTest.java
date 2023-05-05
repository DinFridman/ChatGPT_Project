package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.ChatRepository;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesServiceImplTest {
    @Mock private ChatRepository chatRepository;
    @Mock private ChatMessageMapper chatMessageMapper;
    @Mock private OpenAIPromptDTOMapper openAIPromptDTOMapper;
    @Mock private AppUserServiceImpl appUserService;
    @Captor private ArgumentCaptor<ChatMessageEntity> captor;
    @InjectMocks private MessagesServiceImpl underTest;
    private ChatMessageDTO chatMessageDTO;
    private AppUserEntity expectedAppUser;
    private ChatMessageEntity chatMessageEntity;
    private OpenAIPromptDTO openAIPromptDTO;
    private ConversationDTO expectedConversationDTO;
    private ArrayList<ChatMessageEntity> chatMessageEntities;

    @BeforeEach
    void setUp() {
        Long chatId = 12345L;
        chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .message("Hello")
                .username("Test")
                .updateId(54321L)
                .role(Roles.USER.toString())
                .build();

        expectedAppUser = new AppUserEntity();
        expectedAppUser.setUserId(1L);
        expectedAppUser.setUsername("Test");
        expectedAppUser.setPassword("TestPass");
        expectedAppUser.setLoggedInDate(LocalDateTime.now());

        ArrayList<OpenAIPromptDTO> messagesArray = new ArrayList<>();
        messagesArray.add(
                new OpenAIPromptDTOMapper().mapToOpenAIPromptDTO(
                        chatMessageDTO.getRole(),
                        chatMessageDTO.getMessage()));

        expectedConversationDTO = ConversationDTO.builder()
                .conversation(messagesArray)
                .build();

        chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setMessage(chatMessageDTO.getMessage());
        chatMessageEntity.setChatId(chatMessageDTO.getChatId());
        chatMessageEntity.setConversationRole(chatMessageDTO.getRole());
        chatMessageEntity.setSentDate(LocalDateTime.now());
        chatMessageEntity.setUpdateId(chatMessageDTO.getUpdateId());
        chatMessageEntity.setUser(expectedAppUser);

        chatMessageEntities = new ArrayList<>();
        chatMessageEntities.add(chatMessageEntity);

        openAIPromptDTO = new OpenAIPromptDTO(
                chatMessageEntity.getConversationRole(),
                chatMessageEntity.getMessage());
    }

    @Test
    void shouldAddChatMessage() {
        when(appUserService.getAppUserByUsername("Test")).thenReturn(expectedAppUser);
        when(chatMessageMapper.mapToEntity(chatMessageDTO,expectedAppUser)).thenReturn(chatMessageEntity);

        underTest.addChatMessage(chatMessageDTO);
        verify(chatRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(chatMessageEntity);
    }

    @Test
    void shouldGetAppUser() {
        when(appUserService.getAppUserByUsername("Test")).thenReturn(expectedAppUser);

        AppUserEntity actualAppUser = underTest.getAppUserFromChatMessageDTO(chatMessageDTO);

        assertThat(actualAppUser).isEqualTo(expectedAppUser);
    }

    @Test
    void getConversationByUserId() {
        when(appUserService.getAppUserByUserId(expectedAppUser.getUserId())).thenReturn(expectedAppUser);
        when(openAIPromptDTOMapper.mapToOpenAIPromptDTO(
                chatMessageEntity.getConversationRole(),
                chatMessageEntity.getMessage()))
                .thenReturn(openAIPromptDTO);
        when(chatRepository.findMessagesByUserIdAndDate(
                expectedAppUser.getUserId(),
                expectedAppUser.getLoggedInDate()))
                .thenReturn(chatMessageEntities);

        ConversationDTO actualConversationDTO =
                underTest.getConversationByUserId(expectedAppUser.getUserId());

        assertThat(expectedConversationDTO).isEqualToComparingFieldByField(actualConversationDTO);
    }
}