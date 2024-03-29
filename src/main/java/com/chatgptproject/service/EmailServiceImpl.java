package com.chatgptproject.service;

import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.EmailDetailsDTO;
import com.chatgptproject.dto.mapper.EmailDetailsDTOMapper;
import com.chatgptproject.exception.mail.EmailSendFailureException;
import com.chatgptproject.exception.mail.InvalidEmailProvidedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Objects;

import static com.chatgptproject.utils.constants.TelegramResponseConstants.ERROR_WHILE_SENDING_EMAIL_MESSAGE;
import static com.chatgptproject.utils.constants.TelegramResponseConstants.INVALID_EMAIL_PROVIDED_MESSAGE;

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
                                               String recipient)
    {
        validateEmail(recipient);
        EmailDetailsDTO details = emailDetailsDTOMapper.mapToDTO(conversationDTO, recipient);
        sendSimpleMail(details);
    }

    private void validateEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new InvalidEmailProvidedException(INVALID_EMAIL_PROVIDED_MESSAGE);
    }

    private void sendSimpleMail(EmailDetailsDTO details) {
        try {
            log.debug("Email details: " + details);

            SimpleMailMessage mailMessage = createSimpleMessageDetails(details);

            javaMailSender.send(mailMessage);

            log.info("Email sent successfully.");

        }
        catch (Exception e) {
            throw new EmailSendFailureException(ERROR_WHILE_SENDING_EMAIL_MESSAGE);

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
                    Objects.requireNonNull(file.getFilename()), file);

            log.debug("Mail attachment added successfully.");

            javaMailSender.send(mimeMessage);
            log.info("Mail sent successfully.");
        }

        catch (Exception e) {
            throw new EmailSendFailureException(ERROR_WHILE_SENDING_EMAIL_MESSAGE);
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
