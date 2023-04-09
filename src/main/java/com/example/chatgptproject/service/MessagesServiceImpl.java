package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.AppUserRepository;
import com.example.chatgptproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = {"conversation", "allMessages"})
@Service
@RequiredArgsConstructor
@Log4j2
public class MessagesServiceImpl implements MessagesService {
    private final ChatRepository chatRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;
    private final AppUserServiceImpl appUserService;

    @Caching(evict = {@CacheEvict(value = "allMessages", allEntries = true),
            @CacheEvict(value = "conversation")})
    @Override
    public void addChatMessage(ChatMessageDTO chatMessageDTO) {
        AppUserEntity user = getAppUserFromChatMessageDTO(chatMessageDTO);
        chatRepository.save(chatMessageMapper.mapToEntity(chatMessageDTO,user));
        log.info("---------------ChatMessage added successfully---------------");
        log.debug("---------------ChatMessage added: " + chatMessageDTO + "---------------");
    }

    @Override
    public AppUserEntity getAppUserFromChatMessageDTO(ChatMessageDTO chatMessageDTO) {
        String username = chatMessageDTO.getChatId().toString(); //Appuser`s username is chatId
        return appUserService.getAppUser(username);
    }

    @Cacheable(value = "conversation", key = "#chatId")
    @Override
    public ConversationDTO getConversationById(Long chatId) {//TODO: replace with userId
        ArrayList<OpenAIPromptDTO> messages = (ArrayList<OpenAIPromptDTO>) chatRepository
                .findMessagesByChatId(chatId)
                .stream()
                .map(entity -> openAIPromptDTOMapper
                        .mapToOpenAIPromptDTO(entity.getConversationRole()
                                , entity.getMessage())).collect(Collectors.toList());
        return ConversationDTO.builder()
                .conversation(messages)
                .build();
    }
}
