package com.chatgptproject.exception.mail;

import org.springframework.http.HttpStatus;

public class InvalidEmailProvidedException extends EmailAbstractException {


    public InvalidEmailProvidedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
