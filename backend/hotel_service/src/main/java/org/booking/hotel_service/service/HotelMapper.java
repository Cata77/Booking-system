package org.booking.hotel_service.service;

import org.booking.hotel_service.model.FeatureDTO;
import org.booking.hotel_service.model.Hotel;
import org.booking.hotel_service.model.HotelDTO;
import org.booking.hotel_service.model.RoomDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class HotelMapper {
    public HotelDTO toDTO(Hotel hotel) {
        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .country(hotel.getCountry())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .hotelCategory(hotel.getHotelCategory())
                .accommodationType(hotel.getAccommodationType())
                .propertyType(hotel.getPropertyType())
                .description(hotel.getDescription())
                .checkInTime(hotel.getCheckInTime())
                .checkOutTime(hotel.getCheckOutTime())
                .build();
    }

    public HotelDTO toDTOWithDetails(Hotel hotel, Set<RoomDTO> roomDTOS, Set<FeatureDTO> featureDTOS) {
        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .country(hotel.getCountry())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .hotelCategory(hotel.getHotelCategory())
                .accommodationType(hotel.getAccommodationType())
                .propertyType(hotel.getPropertyType())
                .description(hotel.getDescription())
                .checkInTime(hotel.getCheckInTime())
                .checkOutTime(hotel.getCheckOutTime())
                .rooms(roomDTOS)
                .features(featureDTOS)
                .build();
    }
}
