package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.mapper.EmailDetailsDTOMapper;
import com.example.chatgptproject.exception.mail.MailSendFailureException;
import com.example.chatgptproject.dto.EmailDetailsDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import static com.example.chatgptproject.utils.Constants.SHARE_CONVERSATION_BY_EMAIL_REQUEST;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    private final EmailDetailsDTOMapper emailDetailsDTOMapper;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void handleShareConversationRequest(ConversationDTO conversationDTO,
                                               String messageBody)
    {
        String recipient = getRecipientFromShareConversationRequest(messageBody);
        EmailDetailsDTO details = emailDetailsDTOMapper.mapToDTO(conversationDTO, recipient);
        sendSimpleMail(details);
    }

    @Override
    public String getRecipientFromShareConversationRequest(String messageBody) {
        return messageBody.substring(
                SHARE_CONVERSATION_BY_EMAIL_REQUEST.length()).trim();
    }

    @Override
    public void sendSimpleMail(EmailDetailsDTO details) {
        try {
            log.debug("Email details: " + details);

            SimpleMailMessage mailMessage = createSimpleMessageDetails(details);

            javaMailSender.send(mailMessage);

            log.info("Email sent successfully.");

        }
        catch (Exception e) {
            throw new MailSendFailureException("Error while sending mail!");

        }
    }

    @Override
    public void sendMailWithAttachment(EmailDetailsDTO details)
    {
        try {
            MimeMessage mimeMessage
                    = javaMailSender.createMimeMessage();

            log.debug("Mail details: " + details);

            MimeMessageHelper mimeMessageHelper =
                    createAndSetMimeMessageHelperDetails(mimeMessage,details);


            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            log.debug("Mail attachment details: " + details.getAttachment());

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            log.debug("Mail attachment added successfully.");

            javaMailSender.send(mimeMessage);
            log.info("Mail sent successfully.");
        }

        catch (MessagingException e) {
            throw new MailSendFailureException("Error while sending mail!");
        }
    }

    public SimpleMailMessage createSimpleMessageDetails(EmailDetailsDTO details) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());
        return mailMessage;
    }

    public MimeMessageHelper createAndSetMimeMessageHelperDetails(
            MimeMessage mimeMessage, EmailDetailsDTO details)
            throws MessagingException {

        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(details.getRecipient());
        mimeMessageHelper.setText(details.getMsgBody());
        mimeMessageHelper.setSubject(details.getSubject());
        return mimeMessageHelper;
    }


}
