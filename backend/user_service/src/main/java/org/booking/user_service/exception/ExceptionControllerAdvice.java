package org.booking.user_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(UserAlreadyTakenException.class)
    public ResponseEntity<ErrorDetails> exceptionUserAlreadyTakenHandler() {
        ErrorDetails errorDetails = new ErrorDetails("User already taken!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
