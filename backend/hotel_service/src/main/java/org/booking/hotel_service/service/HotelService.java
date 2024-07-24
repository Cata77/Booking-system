package org.booking.hotel_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.exception.FeatureNotFoundException;
import org.booking.hotel_service.exception.HotelAlreadyCreatedException;
import org.booking.hotel_service.exception.HotelNotFoundException;
import org.booking.hotel_service.exception.RoomNotFoundException;
import org.booking.hotel_service.model.*;
import org.booking.hotel_service.repository.FeatureRepository;
import org.booking.hotel_service.repository.HotelFeatureRepository;
import org.booking.hotel_service.repository.HotelRepository;
import org.booking.hotel_service.repository.RoomRepository;
import org.booking.hotel_service.utils.JwtUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
    private final HotelFeatureMapper hotelFeatureMapper;
    private final FeatureMapper featureMapper;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    @CachePut(cacheNames = "features", key = "#feature.id")
    public FeatureDTO createFeature(Feature feature) {
        featureRepository.save(feature);

        return featureMapper.toDTO(feature);
    }

    @Transactional
    @CachePut(cacheNames = "hotels", key = "#hotel.id")
    public HotelDTO createHotel(Hotel hotel) {
        UUID userId = jwtUtil.extractUserId();

        hotelRepository.findByNameAndAddressAndUserId(hotel.getName(), hotel.getAddress(), userId)
                .ifPresent(existingHotel -> {
                    throw new HotelAlreadyCreatedException();
                });

        hotel.setUserId(userId);
        hotel.setLastUpdate(LocalDateTime.now());
        hotelRepository.save(hotel);

        return hotelMapper.toDTO(hotel);
    }

    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "rooms", key = "#hotelId")},
            evict = {@CacheEvict(cacheNames = "hotels", key = "#hotelId")}
    )
    public RoomDTO createHotelRoom(Room room, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        amendLastUpdate(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);

        return roomMapper.toDTO(room, hotel);
    }

    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "hotel_features", key = "#hotelId")},
            evict = {@CacheEvict(cacheNames = "hotels", key = "#hotelId")}
    )
    public HotelFeatureDTO addHotelFeatures(Long hotelId, Set<Feature> features) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Set<HotelFeature> hotelFeatures = features.stream()
                .map(feature -> createHotelFeature(hotel, feature))
                .collect(Collectors.toSet());

        hotelFeatureRepository.saveAll(hotelFeatures);
        Set<FeatureDTO> featureDTOS = features.stream()
                .map(featureMapper::toDTO)
                .collect(Collectors.toSet());

        amendLastUpdate(hotel);
        return hotelFeatureMapper.toDTO(hotel, featureDTOS);
    }

    @Transactional
    @CachePut(cacheNames = "hotels", key = "#hotelId")
    public HotelDTO updateHotel(Hotel hotel, Long hotelId) {
        hotel.setId(hotelId);
        amendLastUpdate(hotel);
        return hotelMapper.toDTO(hotel);
    }

    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "rooms", key = "#hotelId")},
            evict = {@CacheEvict(cacheNames = "hotels", key = "#hotelId")}
    )
    public RoomDTO updateHotelRoom(Room room, Long roomId, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        amendLastUpdate(hotel);
        room.setId(roomId);
        room.setHotel(hotel);
        roomRepository.save(room);

        return roomMapper.toDTO(room, hotel);
    }

    @Transactional
    @Cacheable(cacheNames = "hotels", key = "#hotelId")
    public HotelDTO viewHotelDetails(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Set<Room> rooms = roomRepository.findByHotelId(hotelId);
        Set<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> roomMapper.toDTO(room, hotel))
                .collect(Collectors.toSet());

        Set<String> featureNames = hotelRepository.findHotelFeatureList(hotelId);
        Set<Feature> features = featureNames.stream()
                .map(featureRepository::findByName)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        Set<FeatureDTO> featureDTOS = features.stream()
                .map(featureMapper::toDTO)
                .collect(Collectors.toSet());

        return hotelMapper.toDTOWithDetails(hotel, roomDTOS, featureDTOS);
    }

    @Transactional
    public Set<RoomDTO> viewHotelRooms(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        return roomRepository.findByHotelId(hotelId).stream()
                .map(room -> roomMapper.toDTO(room, hotel))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Cacheable(cacheNames = "rooms", key = "#hotelId")
    public RoomDTO viewRoom(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        return roomMapper.toDTO(room, hotel);
    }

    @Transactional
    public Set<FeatureDTO> getHotelFeatures(Long hotelId) {
        Set<String> featureNames = hotelRepository.findHotelFeatureList(hotelId);
        Set<Feature> features = featureNames.stream()
                .map(featureRepository::findByName)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return features.stream()
                .map(featureMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "rooms", key = "#hotelId"),
                    @CacheEvict(cacheNames = "hotel_features", key = "#hotelId"),
                    @CacheEvict(cacheNames = "hotels", key = "#hotelId")
            }
    )
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
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "rooms", key = "#hotelId"),
                    @CacheEvict(cacheNames = "hotels", key = "#hotelId")
            }
    )
    public void removeRoom(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        Room room = roomRepository.findById(roomId)
                        .orElseThrow(RoomNotFoundException::new);
        roomRepository.delete(room);

        amendLastUpdate(hotel);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "hotel_features", key = "#hotelId"),
                    @CacheEvict(cacheNames = "hotels", key = "#hotelId")
            }
    )
    public void removeFeature(Long hotelId, Long featureId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(HotelNotFoundException::new);

        HotelFeature hotelFeature = hotelFeatureRepository.findByHotelIdAndFeatureId(hotelId, featureId)
                .orElseThrow(FeatureNotFoundException::new);
        hotelFeatureRepository.delete(hotelFeature);

        amendLastUpdate(hotel);
    }

    private HotelFeature createHotelFeature(Hotel hotel, Feature feature) {
        HotelFeature hotelFeature = new HotelFeature();
        hotelFeature.setHotel(hotel);
        hotelFeature.setFeature(feature);
        return hotelFeature;
    }

    private void amendLastUpdate(Hotel hotel) {
        hotel.setLastUpdate(LocalDateTime.now());
        hotelRepository.save(hotel);
    }
}
