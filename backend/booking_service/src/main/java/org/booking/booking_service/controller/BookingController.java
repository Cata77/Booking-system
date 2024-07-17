package org.booking.booking_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.booking_service.model.Booking;
import org.booking.booking_service.model.BookingDTO;
import org.booking.booking_service.model.BookingUpdateDTO;
import org.booking.booking_service.model.DateRangeDTO;
import org.booking.booking_service.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{bookingId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestBody BookingUpdateDTO bookingUpdateDTO
    ) {
        BookingDTO bookingDTO = service.updateBooking(bookingId, bookingUpdateDTO);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @GetMapping("{roomId}/unavailable-dates")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<List<DateRangeDTO>> getUnavailableDates(@PathVariable("roomId") Long roomId) {
        List<DateRangeDTO> unavailableDates = service.getUnavailableDates(roomId);
        return new ResponseEntity<>(unavailableDates, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable("bookingId") Long bookingId) {
        BookingDTO bookingDTO = service.getBooking(bookingId);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }
}
