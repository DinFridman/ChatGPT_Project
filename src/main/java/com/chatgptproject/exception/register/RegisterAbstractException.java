package com.chatgptproject.exception.register;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class RegisterAbstractException extends RuntimeException{
    private final HttpStatus status;

    public RegisterAbstractException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}

