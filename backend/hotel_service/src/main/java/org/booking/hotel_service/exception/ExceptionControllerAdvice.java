package org.booking.hotel_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(HotelAlreadyCreatedException.class)
    public ResponseEntity<ErrorDetails> exceptionUserAlreadyTakenHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Hotel already created!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
