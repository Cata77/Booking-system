package org.booking.hotel_service.model;

import java.io.Serializable;

public record FeatureDTO(
        Long id,
        String name
) implements Serializable {
}
