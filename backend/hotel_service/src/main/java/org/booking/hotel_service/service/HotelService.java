package org.booking.hotel_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.exception.HotelAlreadyCreatedException;
import org.booking.hotel_service.exception.HotelNotFoundException;
import org.booking.hotel_service.exception.RoomNotFoundException;
import org.booking.hotel_service.model.*;
import org.booking.hotel_service.repository.FeatureRepository;
import org.booking.hotel_service.repository.HotelFeatureRepository;
import org.booking.hotel_service.repository.HotelRepository;
import org.booking.hotel_service.repository.RoomRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final FeatureRepository featureRepository;
    private final HotelFeatureRepository hotelFeatureRepository;

    private Jwt extractJWT() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authenticationToken.getCredentials();
    }

    @CachePut(value = "features", key = "#feature.id")
    public void createFeature(Feature feature) {
        featureRepository.save(feature);
    }

    @Transactional
    @CachePut(value = "hotels", key = "#hotel.id")
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

        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .country(hotel.getCountry())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .hotelCategory(hotel.getHotelCategory())
                .accommodationType(hotel.getAccommodationType())
                .propertyType(hotel.getPropertyType())
                .description(hotel.getDescription())
                .checkInTime(hotel.getCheckInTime())
                .checkOutTime(hotel.getCheckOutTime())
                .build();
    }

    @Transactional
    @CachePut(value = "rooms", key = "#room.id")
    public RoomDTO createHotelRoom(Room room, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        room.setHotel(hotel);
        roomRepository.save(room);

        return new RoomDTO(
                room.getId(),
                hotel.getName(),
                room.getBedroomCount(),
                room.getBedCount(),
                room.getMaxGuestsCount(),
                room.getPrice()
        );
    }

    @Transactional
    @CachePut(value = "hotel_feature", key = "#result.id")
    public HotelFeatureDTO addHotelFeatures(Long hotelId, Set<Feature> features) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Set<HotelFeature> hotelFeatures = features.stream()
                .map(feature -> createHotelFeature(hotel, feature))
                .collect(Collectors.toSet());

        hotelFeatureRepository.saveAll(hotelFeatures);

        Set<String> featureNames = features.stream()
                .map(Feature::getName)
                .collect(Collectors.toSet());

        return new HotelFeatureDTO(
                hotelId,
                hotel.getName(),
                featureNames
        );
    }

    private HotelFeature createHotelFeature(Hotel hotel, Feature feature) {
        HotelFeature hotelFeature = new HotelFeature();
        hotelFeature.setHotel(hotel);
        hotelFeature.setFeature(feature);
        return hotelFeature;
    }

    @Transactional
//    @Cacheable(value = "hotels", key = "#hotelId")
    public HotelDTO viewHotelDetails(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Set<Room> rooms = roomRepository.findByHotelId(hotelId);
        Set<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> createRoomDTO(room, hotel))
                .collect(Collectors.toSet());

        Set<String> features = hotelRepository.findHotelFeatureList();

        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .country(hotel.getCountry())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .hotelCategory(hotel.getHotelCategory())
                .accommodationType(hotel.getAccommodationType())
                .propertyType(hotel.getPropertyType())
                .description(hotel.getDescription())
                .checkInTime(hotel.getCheckInTime())
                .checkOutTime(hotel.getCheckOutTime())
                .rooms(roomDTOS)
                .features(features)
                .build();
    }

    public Set<RoomDTO> viewHotelRooms(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        return roomRepository.findByHotelId(hotelId).stream()
                .map(room -> createRoomDTO(room, hotel))
                .collect(Collectors.toSet());
    }

    private RoomDTO createRoomDTO(Room room, Hotel hotel) {
        return new RoomDTO(
                room.getId(),
                hotel.getName(),
                room.getBedroomCount(),
                room.getBedCount(),
                room.getMaxGuestsCount(),
                room.getPrice());
    }

    @Transactional
    public RoomDTO viewRoom(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        return createRoomDTO(room, hotel);
    }

    @Transactional
    public void removeHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Set<Room> rooms = roomRepository.findByHotelId(hotelId);
        roomRepository.deleteAll(rooms);

        Set<HotelFeature> hotelFeatures = hotelFeatureRepository.findHotelFeatureByHotelId(hotelId);
        hotelFeatureRepository.deleteAll(hotelFeatures);

        hotelRepository.delete(hotel);
    }

    @Transactional
    public void removeRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                        .orElseThrow(RoomNotFoundException::new);
        roomRepository.delete(room);
    }


}
