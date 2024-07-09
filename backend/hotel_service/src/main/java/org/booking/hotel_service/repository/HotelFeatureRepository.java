package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.HotelFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HotelFeatureRepository extends JpaRepository<HotelFeature, Long> {
    Set<HotelFeature> findHotelFeatureByHotelId(Long hotelId);
}
