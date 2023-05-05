package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessagesServiceImpl implements MessagesService {
    private final ChatRepository chatRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;
    private final AppUserServiceImpl appUserService;

    @Override
    public void addChatMessage(ChatMessageDTO chatMessageDTO) {
        AppUserEntity user = getAppUserFromChatMessageDTO(chatMessageDTO);
        chatRepository.save(chatMessageMapper.mapToEntity(chatMessageDTO,user));
        log.info("---------------ChatMessage added successfully---------------");
        log.debug("---------------ChatMessage added: " + chatMessageDTO + "---------------");
    }

    @Override
    public AppUserEntity getAppUserFromChatMessageDTO(ChatMessageDTO chatMessageDTO) {
        String username = extractUsernameFromChatMessageDTO(chatMessageDTO);
        return appUserService.getAppUserByUsername(username);
    }

    private String extractUsernameFromChatMessageDTO(ChatMessageDTO chatMessageDTO) {
        return chatMessageDTO.getUsername();
    }

    @Override
    public ConversationDTO getConversationByUserId(Long userId) {
        ArrayList<ChatMessageEntity> messageEntities = getMessagesArrayFromRepository(userId);
        ArrayList<OpenAIPromptDTO> openAIPromptDTOList =
                mapMessagesArrayToOpenAIPromptArray(messageEntities);
        return createConversationDTOFromOpenAIPromptArray(openAIPromptDTOList);
    }

    private ArrayList<ChatMessageEntity> getMessagesArrayFromRepository(Long userId) {
        AppUserEntity appUser = getAppUserByUserId(userId);
        LocalDateTime loggedInDate = getLoggedInDateFromAppUser(appUser);

        return chatRepository
                .findMessagesByUserIdAndDate(userId, loggedInDate);
    }

    private LocalDateTime getLoggedInDateFromAppUser(AppUserEntity appUser) {
        return appUser.getLoggedInDate();
    }

    private AppUserEntity getAppUserByUserId(Long userId) {
        return appUserService.getAppUserByUserId(userId);
    }

    private ArrayList<OpenAIPromptDTO> mapMessagesArrayToOpenAIPromptArray(
            ArrayList<ChatMessageEntity> messages) {
        return (ArrayList<OpenAIPromptDTO>) messages.stream()
                .map(entity -> openAIPromptDTOMapper
                        .mapToOpenAIPromptDTO(entity.getConversationRole()
                                , entity.getMessage())).collect(Collectors.toList());
    }

    private ConversationDTO createConversationDTOFromOpenAIPromptArray(
            ArrayList<OpenAIPromptDTO> messages) {
        return ConversationDTO.builder()
                .conversation(messages)
                .build();
    }
}
