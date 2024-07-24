package org.booking.hotel_service.service;

import org.booking.hotel_service.model.Hotel;
import org.booking.hotel_service.model.Room;
import org.booking.hotel_service.model.RoomDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomDTO toDTO(Room room, Hotel hotel) {
        return new RoomDTO(
                room.getId(),
                hotel.getName(),
                room.getBedroomCount(),
                room.getBedCount(),
                room.getMaxGuestsCount(),
                room.getPrice()
        );
    }
}
