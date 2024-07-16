package org.booking.booking_service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record BookingDTO(
        Long id,
        UUID userId,
        String hotelName,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        int guestCount,
        RoomDTO room,
        BigDecimal discount,
        BigDecimal payout,
        Status status
) implements Serializable {
}
