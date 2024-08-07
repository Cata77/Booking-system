package org.booking.booking_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.booking.booking_service.utils.JwtUtil;
import org.booking.booking_service.exception.BookingNotFoundException;
import org.booking.booking_service.exception.ExceedsRoomCapacityException;
import org.booking.booking_service.model.*;
import org.booking.booking_service.repository.BookingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelService hotelService;
    private final BookingMapper bookingMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "bookings", key = "#booking.id")},
            evict = {@CacheEvict(cacheNames = "unavailable-dates", key = "#booking.roomId")}
    )
    public BookingDTO createBooking(Booking booking) {
        UUID userId = jwtUtil.extractUserId();
        RoomDTO roomDTO = hotelService.getRoomDetails(booking.getHotelId(), booking.getRoomId());

        validateBooking(booking, roomDTO);

        Booking savedBooking = createAndSaveBooking(booking, userId, roomDTO);

        return bookingMapper.toDTO(savedBooking, roomDTO);
    }

    @Transactional
    @CachePut(cacheNames = "bookings", key = "#bookingId")
    public BookingDTO updateBooking(Long bookingId, BookingUpdateDTO updateDTO) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        if (updateDTO.status() != null && updateDTO.status() == Status.CANCELED) {
            existingBooking.setStatus(Status.CANCELED);
            Booking updatedBooking = bookingRepository.save(existingBooking);
            RoomDTO roomDTO = hotelService.getRoomDetails(updatedBooking.getHotelId(), updatedBooking.getRoomId());
            return bookingMapper.toDTO(updatedBooking, roomDTO);
        }

        boolean needsPayoutRecalculation = false;

        if (updateDTO.checkInDate() != null && !updateDTO.checkInDate().equals(existingBooking.getCheckInDate())) {
            existingBooking.setCheckInDate(updateDTO.checkInDate());
            needsPayoutRecalculation = true;
        }

        if (updateDTO.checkOutDate() != null && !updateDTO.checkOutDate().equals(existingBooking.getCheckOutDate())) {
            existingBooking.setCheckOutDate(updateDTO.checkOutDate());
            needsPayoutRecalculation = true;
        }

        if (updateDTO.guestCount() != null && !updateDTO.guestCount().equals(existingBooking.getGuestCount())) {
            existingBooking.setGuestCount(updateDTO.guestCount());
        }

        if (needsPayoutRecalculation) {
            RoomDTO roomDTO = hotelService.getRoomDetails(existingBooking.getHotelId(), existingBooking.getRoomId());
            validateBooking(existingBooking, roomDTO);

            long bookedDays = ChronoUnit.DAYS.between(existingBooking.getCheckInDate(), existingBooking.getCheckOutDate());
            BigDecimal payout = calculatePayout(bookedDays, roomDTO.price());
            BigDecimal discount = calculateDiscount(payout, bookedDays);

            existingBooking.setDiscount(discount);
            existingBooking.setPayout(payout.subtract(discount));
        }

        Booking updatedBooking = bookingRepository.save(existingBooking);
        RoomDTO roomDTO = hotelService.getRoomDetails(updatedBooking.getHotelId(), updatedBooking.getRoomId());

        return bookingMapper.toDTO(updatedBooking, roomDTO);
    }

    @Cacheable(cacheNames = "unavailable-dates", key = "#roomId")
    public List<DateRangeDTO> getUnavailableDates(Long roomId) {
        return bookingRepository.getBookingDatesByRoomId(roomId);
    }

    @Transactional
    @Cacheable(cacheNames = "bookings", key = "#bookingId")
    public BookingDTO getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        RoomDTO roomDTO = hotelService.getRoomDetails(booking.getHotelId(), booking.getRoomId());

        return bookingMapper.toDTO(booking, roomDTO);
    }

    private void validateBooking(Booking booking, RoomDTO roomDTO) {
        if (booking.getGuestCount() > roomDTO.maxGuestsCount()) {
            throw new ExceedsRoomCapacityException();
        }
    }

    private Booking createAndSaveBooking(Booking booking, UUID userId, RoomDTO roomDTO) {
        long bookedDays = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal payout = calculatePayout(bookedDays, roomDTO.price());
        BigDecimal discount = calculateDiscount(payout, bookedDays);

        booking.setUserId(userId);
        booking.setDiscount(discount);
        booking.setPayout(payout.subtract(discount));
        booking.setStatus(Status.ACCEPTED);

        return bookingRepository.save(booking);
    }

    private BigDecimal calculatePayout(long bookedDays, BigDecimal roomPrice) {
        return BigDecimal.valueOf(bookedDays).multiply(roomPrice);
    }

    private BigDecimal calculateDiscount(BigDecimal payout, long bookedDays) {
        if (bookedDays >= 7) {
            return payout.multiply(BigDecimal.valueOf(0.05));
        }
        return BigDecimal.ZERO;
    }
}
