package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    Optional<Hotel> findByNameAndAddressAndUserId(String name, String address, UUID userId);
}
