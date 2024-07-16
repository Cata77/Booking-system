package org.booking.booking_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ExceedsRoomCapacityException.class)
    public ResponseEntity<ErrorDetails> exceptionExceedsRoomCapacityHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Number of guests exceeds the room's maximum capacity!");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDetails);
    }

    @ExceptionHandler(DependentServiceNotAvailableException.class)
    public ResponseEntity<ErrorDetails> dependentServiceNotAvailableHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Dependent service is not available!");
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorDetails);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorDetails> bookingNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails("Booking not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }
}
