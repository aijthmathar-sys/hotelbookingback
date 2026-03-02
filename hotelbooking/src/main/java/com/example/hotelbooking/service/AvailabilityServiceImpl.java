package com.example.hotelbooking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);

        return conflicts.isEmpty();
    }
    @Override
public List<Room> getAvailableRooms(String city, LocalDate checkIn, LocalDate checkOut) {

    List<Room> rooms = roomRepository.findRoomsByCity(city);

    return rooms.stream()
            .filter(room -> isRoomAvailable(room.getId(), checkIn, checkOut))
            .toList();
}
}