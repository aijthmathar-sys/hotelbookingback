package com.example.hotelbooking.service;

import com.example.hotelbooking.entity.Room;
import java.util.List;

public interface RoomService {

    Room createRoom(Long hotelId, Room room);

    List<Room> getRoomsByHotel(Long hotelId);

    Room updateRoom(Long id, Room room);

    void deleteRoom(Long id);
}