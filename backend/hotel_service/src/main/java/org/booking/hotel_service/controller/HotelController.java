package org.booking.hotel_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.model.*;
import org.booking.hotel_service.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService service;

    @PostMapping("/features")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> addNewHotelFeature(@RequestBody Feature feature) {
        service.createFeature(feature);
        return new ResponseEntity<>("Feature created!", HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<HotelDTO> addNewHotel(@RequestBody Hotel hotel) {
        HotelDTO createdHotel = service.createHotel(hotel);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    @PostMapping("/{hotel_id}/rooms")
    @PreAuthorize("hasRole('owner')")
    public ResponseEntity<RoomDTO> addRoomsToHotel(
            @RequestBody Room room,
            @PathVariable("hotel_id") Long hotelId) {
        RoomDTO createdRoom = service.createHotelRoom(room, hotelId);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }
}
