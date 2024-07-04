package org.booking.hotel_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.exception.HotelAlreadyCreatedException;
import org.booking.hotel_service.model.Feature;
import org.booking.hotel_service.model.Hotel;
import org.booking.hotel_service.model.HotelDTO;
import org.booking.hotel_service.repository.FeatureRepository;
import org.booking.hotel_service.repository.HotelRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final FeatureRepository featureRepository;
    private final HotelRepository hotelRepository;

    private Jwt extractJWT() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authenticationToken.getCredentials();
    }

    @CachePut(value = "features", key = "#feature.id")
    public void createFeature(Feature feature) {
        featureRepository.save(feature);
    }

    @Transactional
    @CachePut(value = "hotels", key = "#hotel.uuid")
    public HotelDTO createHotel(Hotel hotel) {
        Jwt jwt = extractJWT();
        String subject = (String) jwt.getClaims().get("sub");
        UUID userId = UUID.fromString(subject);

        hotelRepository.findByNameAndAddressAndUserId(hotel.getName(), hotel.getAddress(), userId)
                .ifPresent(existingHotel -> {
                    throw new HotelAlreadyCreatedException();
                });

        hotel.setUserId(userId);
        hotelRepository.save(hotel);

        return new HotelDTO(
                hotel.getName(),
                hotel.getCountry(),
                hotel.getCity(),
                hotel.getAddress(),
                hotel.getHotelCategory(),
                hotel.getAccommodationType(),
                hotel.getPropertyType(),
                hotel.getDescription(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime()
        );
    }
}
