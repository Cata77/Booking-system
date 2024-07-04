package org.booking.hotel_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.hotel_service.model.Feature;
import org.booking.hotel_service.model.Hotel;
import org.booking.hotel_service.model.HotelDTO;
import org.booking.hotel_service.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        HotelDTO createdHotel = service.createHotel(jwt, hotel);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }
}
