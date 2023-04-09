package com.example.chatgptproject.dto.mapper;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.EmailDetailsDTO;
import com.example.chatgptproject.utils.Constants;

public class EmailDetailsDTOMapper {
    public EmailDetailsDTO mapToDTO(ConversationDTO conversationDTO, String recipient) {
        return EmailDetailsDTO.builder()
                .recipient(recipient)
                .msgBody(conversationDTO.toString())
                .subject(Constants.EMAIL_SUBJECT)
                .build();
    }
}
