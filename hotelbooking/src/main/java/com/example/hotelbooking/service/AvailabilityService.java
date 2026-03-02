package com.example.hotelbooking.service;

import java.time.LocalDate;
import java.util.List;

import com.example.hotelbooking.entity.Room;

public interface AvailabilityService {
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
    List<Room> getAvailableRooms(String city, LocalDate checkIn, LocalDate checkOut);
}
