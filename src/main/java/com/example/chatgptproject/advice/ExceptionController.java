package com.example.chatgptproject.advice;

import com.example.chatgptproject.exception.mail.EmailSendFailureException;
import com.example.chatgptproject.exception.register.RegisterAbstractException;
import com.example.chatgptproject.exception.register.UserIsRegisteredException;
import com.example.chatgptproject.model.ErrorBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = UserIsRegisteredException.class)
    public ResponseEntity<ErrorBody> handleException(RegisterAbstractException exception) {
        return new ResponseEntity<>(
                new ErrorBody(exception.getStatus().name(),
                        exception.getStatus().value(),
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmailSendFailureException.class)
    public ResponseEntity<ErrorBody> handleException(EmailSendFailureException exception) {
        return new ResponseEntity<>(
                new ErrorBody(exception.getStatus().name(),
                        exception.getStatus().value(),
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<String> handleException(BadCredentialsException exception) {
        return new ResponseEntity<>(exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
