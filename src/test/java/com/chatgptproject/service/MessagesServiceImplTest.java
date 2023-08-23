package com.chatgptproject.service;

import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.chatgptproject.model.AppUserEntity;
import com.chatgptproject.model.ChatMessageEntity;
import com.chatgptproject.model.mapper.ChatMessageMapper;
import com.chatgptproject.repository.ChatRepository;
import com.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Mockito.when(appUserService.getAppUserByUsername("Test")).thenReturn(expectedAppUser);
        Mockito.when(chatMessageMapper.mapToEntity(chatMessageDTO,expectedAppUser)).thenReturn(chatMessageEntity);

        underTest.addChatMessage(chatMessageDTO);
        verify(chatRepository).save(captor.capture());
        assertEquals(captor.getValue(),chatMessageEntity);
    }

    @Test
    void shouldGetAppUser() {
        Mockito.when(appUserService.getAppUserByUsername("Test")).thenReturn(expectedAppUser);

        AppUserEntity actualAppUser = underTest.getAppUserFromChatMessageDTO(chatMessageDTO);

        assertEquals(actualAppUser,expectedAppUser);
    }

    @Test
    void getConversationByUserId() {
        Mockito.when(appUserService.getAppUserByUserId(expectedAppUser.getUserId())).thenReturn(expectedAppUser);
        Mockito.when(openAIPromptDTOMapper.mapToOpenAIPromptDTO(
                chatMessageEntity.getConversationRole(),
                chatMessageEntity.getMessage()))
                .thenReturn(openAIPromptDTO);
        Mockito.when(chatRepository.findMessagesByUserIdAndDate(
                expectedAppUser.getUserId(),
                expectedAppUser.getLoggedInDate()))
                .thenReturn(chatMessageEntities);

        ConversationDTO actualConversationDTO =
                underTest.getConversationByUserId(expectedAppUser.getUserId());

        assertEquals(expectedConversationDTO,actualConversationDTO);
    }
}