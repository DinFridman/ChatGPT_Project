package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.EmailDetailsDTO;
import com.example.chatgptproject.dto.mapper.EmailDetailsDTOMapper;
import com.example.chatgptproject.exception.mail.InvalidEmailProvidedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock private JavaMailSender javaMailSender;
    @Mock private EmailDetailsDTOMapper emailDetailsDTOMapper;
    @InjectMocks EmailServiceImpl underTest;
    @Captor ArgumentCaptor<SimpleMailMessage> simpleMailMessageCaptor;
    private EmailDetailsDTO emailDetailsDTO;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void HandleShareConversationRequestWithInvalidEmailShouldThrowException() {
        String recipient = "invalid-email";
        ConversationDTO conversationDTO = mock(ConversationDTO.class);

        Assertions.assertThrows(InvalidEmailProvidedException.class, () -> {
            underTest.handleShareConversationRequest(conversationDTO, recipient);
        });
    }

    @Test
    public void shouldHandleShareConversationRequestWithValidEmail() {
        String recipient = "valid-email@example.com";
        ConversationDTO conversationDTO = mock(ConversationDTO.class);

        EmailDetailsDTO emailDetailsDTO = mock(EmailDetailsDTO.class);
        when(emailDetailsDTOMapper.mapToDTO(conversationDTO, recipient))
                .thenReturn(emailDetailsDTO);

        underTest.handleShareConversationRequest(conversationDTO, recipient);

        verify(javaMailSender).send(simpleMailMessageCaptor.capture());

        SimpleMailMessage sentMessage = simpleMailMessageCaptor.getValue();
        Assertions.assertEquals(
                underTest.createSimpleMessageDetails(emailDetailsDTO), sentMessage);
    }
}