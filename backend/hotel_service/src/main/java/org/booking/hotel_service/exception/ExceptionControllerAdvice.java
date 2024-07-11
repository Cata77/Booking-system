package org.booking.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(HotelAlreadyCreatedException.class)
    public ResponseEntity<ErrorDetails> exceptionHotelAlreadyCreatedHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Hotel already created!");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDetails);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionHotelNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Hotel not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionRoomNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Room not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }

    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionFeatureNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Feature not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }
}
