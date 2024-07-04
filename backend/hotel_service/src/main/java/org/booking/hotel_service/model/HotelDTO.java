package org.booking.hotel_service.model;

import java.io.Serializable;
import java.time.LocalTime;

public record HotelDTO (
        String name,
        String country,
        String city,
        String address,
        HotelCategory hotelCategory,
        AccommodationType accommodationType,
        PropertyType propertyType,
        String description,
        LocalTime checkInTime,
        LocalTime checkOutTime
) implements Serializable {
}
