package org.booking.hotel_service.model;

import java.io.Serializable;
import java.util.Set;

public record HotelFeatureDTO(
        Long id,
        String hotelName,
        Set<String> features
) implements Serializable {
}
