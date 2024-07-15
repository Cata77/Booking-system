package org.booking.booking_service.service;

import jakarta.transaction.Transactional;
import org.booking.booking_service.model.Booking;
import org.booking.booking_service.model.BookingDTO;
import org.booking.booking_service.model.RoomDTO;
import org.booking.booking_service.repository.BookingRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
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
    @CachePut(cacheNames = "bookings", key = "#booking.id")
    public BookingDTO createBooking(Booking booking) {
        Jwt jwt = extractJWT();
        String subject = (String) jwt.getClaims().get("sub");
        UUID userId = UUID.fromString(subject);

        RestClient authenticatedClient = restClientBuilder
                .defaultHeader("Authorization", "Bearer " + jwt.getTokenValue())
                .build();

        RoomDTO roomDTO = authenticatedClient.get()
                .uri("/{hotel_id}/rooms/{room_id}", booking.getHotelId(), booking.getRoomId())
                .retrieve()
                .body(RoomDTO.class);

        //future payout calculation and exception handling

        booking.setUserId(userId);
        bookingRepository.save(booking);

        return new BookingDTO(
                booking.getId(),
                userId,
                roomDTO.hotelName(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestCount(),
                roomDTO,
                new BigDecimal(200)
        );
    }
}
