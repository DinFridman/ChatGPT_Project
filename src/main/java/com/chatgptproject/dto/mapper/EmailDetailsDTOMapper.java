package com.chatgptproject.dto.mapper;

import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.EmailDetailsDTO;
import com.chatgptproject.utils.constants.Constants;

public class EmailDetailsDTOMapper {
    public EmailDetailsDTO mapToDTO(ConversationDTO conversationDTO, String recipient) {
        return EmailDetailsDTO.builder()
                .recipient(recipient)
                .msgBody(conversationDTO.toString())
                .subject(Constants.EMAIL_SUBJECT)
                .build();
    }
}
