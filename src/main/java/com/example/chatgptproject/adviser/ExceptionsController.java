package com.example.chatgptproject.adviser;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionsController {

    /*@ExceptionHandler(value = DivideByZeroException.class)
    public ResponseEntity<ResponseModel> exception(DivideByZeroException exception) {
        ResponseModel failureResponseModel = new ResponseModel(exception.getMessage());
        return new ResponseEntity<>(failureResponseModel, HttpStatus.CONFLICT);
    }*/
}
