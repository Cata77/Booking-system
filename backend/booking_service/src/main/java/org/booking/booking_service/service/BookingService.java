package org.booking.booking_service.service;

import jakarta.transaction.Transactional;
import org.booking.booking_service.exception.BookingNotFoundException;
import org.booking.booking_service.exception.DependentServiceNotAvailableException;
import org.booking.booking_service.exception.ExceedsRoomCapacityException;
import org.booking.booking_service.model.*;
import org.booking.booking_service.repository.BookingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestClient.Builder restClientBuilder;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.restClientBuilder = RestClient.builder()
                .baseUrl("http://localhost:8222/hotels");
    }

    private Jwt extractJWT() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authenticationToken.getCredentials();
    }

    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "bookings", key = "#booking.id")},
            evict = {@CacheEvict(cacheNames = "unavailable-dates", key = "#booking.roomId")}
    )
    public BookingDTO createBooking(Booking booking) {
        Jwt jwt = extractJWT();
        String subject = (String) jwt.getClaims().get("sub");
        UUID userId = UUID.fromString(subject);

        RestClient authenticatedClient = restClientBuilder
                .defaultHeader("Authorization", "Bearer " + jwt.getTokenValue())
                .build();

        try {
            RoomDTO roomDTO = authenticatedClient.get()
                    .uri("/{hotel_id}/rooms/{room_id}", booking.getHotelId(), booking.getRoomId())
                    .retrieve()
                    .body(RoomDTO.class);

            Optional<RoomDTO> optionalRoomDTO = Optional.ofNullable(roomDTO);
            optionalRoomDTO.orElseThrow(() -> new ServiceUnavailableException("Unable to retrieve room information"));

            if (booking.getGuestCount() > roomDTO.maxGuestsCount()) {
                throw new ExceedsRoomCapacityException();
            }

            long bookedDays = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            BigDecimal payout = BigDecimal.valueOf(bookedDays).multiply(roomDTO.price());
            BigDecimal discount = BigDecimal.ZERO;
            if (bookedDays >= 7) {
                discount = (payout.multiply(BigDecimal.valueOf(0.05)));
                payout = payout.subtract(discount);
            }

            booking.setUserId(userId);
            booking.setDiscount(discount);
            booking.setPayout(payout);
            booking.setStatus(Status.ACCEPTED);
            bookingRepository.save(booking);

            return new BookingDTO(
                    booking.getId(),
                    userId,
                    roomDTO.hotelName(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getGuestCount(),
                    roomDTO,
                    discount,
                    payout,
                    booking.getStatus()
            );
        } catch (ExceedsRoomCapacityException e) {
            throw e;
        } catch (Exception e) {
            throw new DependentServiceNotAvailableException();
        }
    }

    @Cacheable(cacheNames = "unavailable-dates", key = "#roomId")
    public List<DateRangeDTO> getUnavailableDates(Long roomId) {
        return bookingRepository.getBookingDatesByRoomId(roomId);
    }

    @Transactional
    @CachePut(cacheNames = "bookings", key = "#bookingId")
    public BookingDTO getBooking(Long bookingId) throws ServiceUnavailableException {
        Jwt jwt = extractJWT();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        RestClient authenticatedClient = restClientBuilder
                .defaultHeader("Authorization", "Bearer " + jwt.getTokenValue())
                .build();

        RoomDTO roomDTO = authenticatedClient.get()
                .uri("/{hotel_id}/rooms/{room_id}", booking.getHotelId(), booking.getRoomId())
                .retrieve()
                .body(RoomDTO.class);

        Optional<RoomDTO> optionalRoomDTO = Optional.ofNullable(roomDTO);
        optionalRoomDTO.orElseThrow(() -> new ServiceUnavailableException("Unable to retrieve room information"));

        return new BookingDTO(
                bookingId,
                booking.getUserId(),
                roomDTO.hotelName(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestCount(),
                roomDTO,
                booking.getDiscount(),
                booking.getPayout(),
                booking.getStatus()
        );
    }
}
