package com.example.chatgptproject.exception.register;

import org.springframework.http.HttpStatus;

public abstract class RegisterAbstractException extends RuntimeException{
    private HttpStatus status;

    public RegisterAbstractException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

