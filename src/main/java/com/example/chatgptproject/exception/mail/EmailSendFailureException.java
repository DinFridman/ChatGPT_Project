package com.example.chatgptproject.exception.mail;

import org.springframework.http.HttpStatus;

public class EmailSendFailureException extends EmailAbstractException {


    public EmailSendFailureException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
