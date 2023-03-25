package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.AppUserRepository;
import com.example.chatgptproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessagesService {
    private final ChatRepository chatRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;
    private final AppUserRepository appUserRepository;

    public void addChatMessage(ChatMessageDTO chatMessageDTO) {
        AppUser user = getAppUserFromChatMessageDTO(chatMessageDTO);
        chatRepository.save(chatMessageMapper.mapToEntity(chatMessageDTO,user));
        log.info("---------------ChatMessage added successfully---------------");
        log.debug("---------------ChatMessage added: " + chatMessageDTO + "---------------");
    }

    public AppUser getAppUserFromChatMessageDTO(ChatMessageDTO chatMessageDTO) {
        Optional<AppUser> user =
                appUserRepository.findAppUserByUsername(chatMessageDTO.getChatId().toString());
        if(user.isEmpty())
            throw new UsernameNotFoundException("Username is not found!");
        return user.get();
    }

    public ConversationDTO addChatMessageAndGetConversation(ChatMessageDTO chatMessageDTO) {
        addChatMessage(chatMessageDTO);
        ArrayList<OpenAIPromptDTO> messages = getConversationById(chatMessageDTO.getChatId());

        log.info("---------------Conversation returned successfully---------------");
        log.debug("---------------Conversation returned: " + messages + "---------------");

        return ConversationDTO.builder()
                .conversation(messages)
                .build();

    }

    public ArrayList<OpenAIPromptDTO> getConversationById(Long chatId) {//TODO: replace with userId
        return (ArrayList<OpenAIPromptDTO>) chatRepository
                .findMessagesByChatId(chatId)
                .stream()
                .map(entity -> openAIPromptDTOMapper
                        .mapToOpenAIPromptDTO(entity.getConversationRole()
                                , entity.getMessage())).collect(Collectors.toList());

    }


}
