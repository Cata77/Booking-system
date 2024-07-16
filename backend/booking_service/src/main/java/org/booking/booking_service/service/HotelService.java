package org.booking.booking_service.service;

import org.booking.booking_service.config.JwtUtil;
import org.booking.booking_service.exception.DependentServiceNotAvailableException;
import org.booking.booking_service.model.RoomDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HotelService {

    private final RestClient restClient;

    public HotelService(@Value("${hotel.service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public RoomDTO getRoomDetails(Long hotelId, Long roomId) {
        try {
            return restClient.get()
                    .uri("/{hotel_id}/rooms/{room_id}", hotelId, roomId)
                    .header("Authorization", "Bearer " + JwtUtil.getCurrentToken())
                    .retrieve()
                    .body(RoomDTO.class);
        } catch (Exception e) {
            throw new DependentServiceNotAvailableException();
        }
    }
}