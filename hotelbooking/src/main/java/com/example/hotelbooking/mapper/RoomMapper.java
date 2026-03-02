package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.RoomResponseDTO;
import com.example.hotelbooking.entity.Room;

public class RoomMapper {

    public static RoomResponseDTO toDTO(Room room) {

        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setRoomId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPrice(room.getPrice());
        dto.setHotelName(room.getHotel().getName());
        dto.setCity(room.getHotel().getCity());

        return dto;
    }
}