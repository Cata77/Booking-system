package org.booking.hotel_service.model;

import java.io.Serializable;
import java.math.BigDecimal;

public record RoomDTO(
        long id,
        String hotelName,
        int bedroomCount,
        int bedCount,
        int maxGuestsCount,
        BigDecimal price
) implements Serializable {
}
