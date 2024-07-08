package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.HotelFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelFeatureRepository extends JpaRepository<HotelFeature, Long> {
}
