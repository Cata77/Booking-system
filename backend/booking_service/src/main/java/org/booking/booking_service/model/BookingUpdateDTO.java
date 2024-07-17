package org.booking.booking_service.model;

import java.time.LocalDate;

public record BookingUpdateDTO(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer guestCount,
        Status status
) {
}
