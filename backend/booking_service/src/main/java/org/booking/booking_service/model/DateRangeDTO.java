package org.booking.booking_service.model;

import java.io.Serializable;
import java.time.LocalDate;

public record DateRangeDTO(
        LocalDate checkInDate,
        LocalDate checkOutDate
) implements Serializable {
}
