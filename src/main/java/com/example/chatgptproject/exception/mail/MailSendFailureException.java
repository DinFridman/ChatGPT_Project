package com.example.chatgptproject.exception.mail;

import org.springframework.http.HttpStatus;

public class MailSendFailureException extends MailAbstractException {


    public MailSendFailureException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
