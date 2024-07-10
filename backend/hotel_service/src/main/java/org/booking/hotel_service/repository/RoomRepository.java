package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Set<Room> findByHotelId(Long hotelId);
//    Optional<Room> findById(Long roomId);
}
