package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.model.mapper.ChatMessageMapper;
import com.example.chatgptproject.repository.ChatRepository;
import com.example.chatgptproject.utils.enums.Roles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class MessagesService {
    private static final Logger logger = LogManager.getLogger("MessageService-logger");
    private final ChatRepository chatRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;

    public MessagesService(ChatRepository chatRepository,
                           ChatMessageMapper chatMessageMapper,
                           OpenAIPromptDTOMapper openAIPromptDTOMapper) {
        this.chatRepository = chatRepository;
        this.chatMessageMapper = chatMessageMapper;
        this.openAIPromptDTOMapper = openAIPromptDTOMapper;
    }

    public void addChatMessage(ChatMessageDTO chatMessageDTO) {
        chatRepository.save(chatMessageMapper.mapToEntity(chatMessageDTO));
        logger.info("---------------ChatMessage added successfully---------------");
        logger.debug("---------------ChatMessage added: " + chatMessageDTO + "---------------");
    }

    public ConversationDTO addChatMessageAndGetConversation(ChatMessageDTO chatMessageDTO) {
        addChatMessage(chatMessageDTO);
        ArrayList<OpenAIPromptDTO> messages = getConversationById(chatMessageDTO.getChatId());

        logger.info("---------------Conversation returned successfully---------------");
        logger.debug("---------------Conversation returned: " + messages + "---------------");

        return ConversationDTO.builder()
                .conversation(messages)
                .build();

    }

    public ArrayList<OpenAIPromptDTO> getConversationById(Long chatId) {//TODO: replace with userId
        return (ArrayList<OpenAIPromptDTO>) chatRepository
                .findMessagesByChatId(chatId)
                .stream()
                .map(entity -> openAIPromptDTOMapper
                        .mapToOpenAIPromptDTO(entity.getRole()
                                , entity.getMessage())).collect(Collectors.toList());

    }


}
