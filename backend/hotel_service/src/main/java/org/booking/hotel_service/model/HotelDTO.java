package org.booking.hotel_service.model;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@Builder
public record HotelDTO (
        Long id,
        String name,
        String country,
        String city,
        String address,
        HotelCategory hotelCategory,
        AccommodationType accommodationType,
        PropertyType propertyType,
        String description,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        Set<RoomDTO> rooms,
        Set<String> features
) implements Serializable {
}
