package com.example.chatgptproject.adviser;

import com.example.chatgptproject.exception.register.RegisterAbstractException;
import com.example.chatgptproject.exception.register.UserIsRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(value = UserIsRegisteredException.class)
    public ResponseEntity<ErrorBody> handleException(RegisterAbstractException exception) {
        return new ResponseEntity<>(
                new ErrorBody(exception.getStatus().name(),
                        exception.getStatus().value(),
                        exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    class ErrorBody{
        private String error;
        private int status;
        private String message;

        public ErrorBody(String error, int status, String message) {
            this.error = error;
            this.status = status;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
