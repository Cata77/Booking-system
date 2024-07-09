package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByNameAndAddressAndUserId(String name, String address, UUID userId);

    @Query(value = """
            SELECT feature.name
            FROM hotel
            JOIN hotel_feature hf
            ON hotel.id = hf.hotel_id
            JOIN feature
            ON hf.feature_id = feature.id
            """, nativeQuery = true)
    Set<String> findHotelFeatureList();
}
