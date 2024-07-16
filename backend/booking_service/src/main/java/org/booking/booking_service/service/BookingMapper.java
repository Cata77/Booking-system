package org.booking.booking_service.service;

import org.booking.booking_service.model.Booking;
import org.booking.booking_service.model.BookingDTO;
import org.booking.booking_service.model.RoomDTO;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingDTO toDTO(Booking booking, RoomDTO roomDTO) {
        return new BookingDTO(
                booking.getId(),
                booking.getUserId(),
                roomDTO.hotelName(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestCount(),
                roomDTO,
                booking.getDiscount(),
                booking.getPayout(),
                booking.getStatus()
        );
    }
}