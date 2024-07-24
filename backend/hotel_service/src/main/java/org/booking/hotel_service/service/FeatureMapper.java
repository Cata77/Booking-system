package org.booking.hotel_service.service;

import org.booking.hotel_service.model.Feature;
import org.booking.hotel_service.model.FeatureDTO;
import org.springframework.stereotype.Component;

@Component
public class FeatureMapper {
    public FeatureDTO toDTO(Feature feature) {
        return new FeatureDTO(
                feature.getId(),
                feature.getName()
        );
    }
}
