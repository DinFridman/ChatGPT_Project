package com.example.chatgptproject.exception.mail;

import org.springframework.http.HttpStatus;

public abstract class EmailAbstractException extends RuntimeException {
    private HttpStatus status;

    public EmailAbstractException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
