package com.chatgptproject.exception.mail;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class EmailAbstractException extends RuntimeException {
    private final HttpStatus status;

    public EmailAbstractException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
