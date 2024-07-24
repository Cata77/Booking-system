package org.booking.hotel_service.service;

import org.booking.hotel_service.model.FeatureDTO;
import org.booking.hotel_service.model.Hotel;
import org.booking.hotel_service.model.HotelFeatureDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class HotelFeatureMapper {
    public HotelFeatureDTO toDTO(Hotel hotel, Set<FeatureDTO> featureDTOS) {
        return new HotelFeatureDTO(
                hotel.getId(),
                hotel.getName(),
                featureDTOS
        );
    }
}
