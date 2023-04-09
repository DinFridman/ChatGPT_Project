package com.example.chatgptproject.exception.mail;

import org.springframework.http.HttpStatus;

public abstract class MailAbstractException extends RuntimeException {
    private HttpStatus status;

    public MailAbstractException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
