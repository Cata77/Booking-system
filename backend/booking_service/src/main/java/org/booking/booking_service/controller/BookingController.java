package org.booking.booking_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.booking_service.model.Booking;
import org.booking.booking_service.model.BookingDTO;
import org.booking.booking_service.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody Booking booking) {
        BookingDTO bookingDTO = service.createBooking(booking);
        return new ResponseEntity<>(bookingDTO, HttpStatus.CREATED);
    }
}
