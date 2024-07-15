package org.booking.booking_service.repository;

import org.booking.booking_service.model.Booking;
import org.booking.booking_service.model.DateRangeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = """
            SELECT new org.booking.booking_service.model.DateRangeDTO(b.checkInDate, b.checkOutDate)
            FROM Booking b
            WHERE b.status = 'ACCEPTED' AND b.roomId = :roomId
            ORDER BY b.checkInDate
            """)
    List<DateRangeDTO> getBookingDatesByRoomId(@Param("roomId") Long roomId);
}
