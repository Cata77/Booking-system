package org.booking.hotel_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.model.*;
import org.booking.hotel_service.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService service;

    @PostMapping("/features")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<FeatureDTO> addNewHotelFeature(@RequestBody Feature feature) {
        FeatureDTO featureDTO = service.createFeature(feature);
        return new ResponseEntity<>(featureDTO, HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<HotelDTO> addNewHotel(@RequestBody Hotel hotel) {
        HotelDTO createdHotel = service.createHotel(hotel);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    @PostMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<RoomDTO> addHotelRooms(
            @PathVariable("hotelId") Long hotelId,
            @RequestBody Room room
    ) {
        RoomDTO createdRoom = service.createHotelRoom(room, hotelId);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PostMapping("/{hotelId}/features")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<HotelFeatureDTO> addHotelFeatures(
            @PathVariable("hotelId") Long hotelId,
            @RequestBody Set<Feature> features
    ) {
        HotelFeatureDTO hotelFeature = service.addHotelFeatures(hotelId, features);
        return new ResponseEntity<>(hotelFeature, HttpStatus.CREATED);
    }

    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<HotelDTO> updateHotel(
            @RequestBody Hotel hotel,
            @PathVariable("hotelId") Long hotelId
    ) {
        HotelDTO hotelDTO = service.updateHotel(hotel, hotelId);
        return new ResponseEntity<>(hotelDTO, HttpStatus.OK);
    }

    @PutMapping("/{hotelId}/rooms/{roomId}")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<RoomDTO> updateHotelRoom(
            @RequestBody Room room,
            @PathVariable("roomId") Long roomId,
            @PathVariable("hotelId") Long hotelId
    ) {
        RoomDTO roomDTO = service.updateHotelRoom(room, roomId, hotelId);
        return new ResponseEntity<>(roomDTO, HttpStatus.OK);
    }

    @GetMapping("/{hotelId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<HotelDTO> viewHotel(@PathVariable("hotelId") Long hotelId) {
        HotelDTO hotelDTO = service.viewHotelDetails(hotelId);
        return new ResponseEntity<>(hotelDTO, HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Set<RoomDTO>> viewHotelRooms(@PathVariable("hotelId") Long hotelId) {
        Set<RoomDTO> rooms = service.viewHotelRooms(hotelId);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/rooms/{roomId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<RoomDTO> viewRoom(
            @PathVariable("hotelId") Long hotelId,
            @PathVariable("roomId") Long roomId
    ) {
        RoomDTO room = service.viewRoom(hotelId, roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/features")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Set<FeatureDTO>> viewHotelFeatures(
            @PathVariable("hotelId") Long hotelId
    ) {
        Set<FeatureDTO> features = service.getHotelFeatures(hotelId);
        return new ResponseEntity<>(features, HttpStatus.OK);
    }


    @DeleteMapping("/{hotelId}")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<Void> removeHotel(@PathVariable("hotelId") Long hotelId) {
        service.removeHotel(hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{hotelId}/rooms/{roomId}")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<Void> removeRoom(
            @PathVariable("hotelId") Long hotelId,
            @PathVariable("roomId") Long roomId
    ) {
        service.removeRoom(hotelId, roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{hotelId}/features/{featureId}")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<Void> removeFeature(
            @PathVariable("hotelId") Long hotelId,
            @PathVariable("featureId") Long featureId
    ) {
        service.removeFeature(hotelId, featureId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
