package com.example.hotelbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.service.AvailabilityService;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/room")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut
    ) {

        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        boolean available = availabilityService
                .isRoomAvailable(roomId, checkInDate, checkOutDate);

        return ResponseEntity.ok(available);
    }
    @GetMapping("/search")
public ResponseEntity<List<Room>> searchAvailableRooms(
        @RequestParam String city,
        @RequestParam String checkIn,
        @RequestParam String checkOut
) {

    LocalDate checkInDate = LocalDate.parse(checkIn);
    LocalDate checkOutDate = LocalDate.parse(checkOut);

    List<Room> rooms = availabilityService
            .getAvailableRooms(city, checkInDate, checkOutDate);

    return ResponseEntity.ok(rooms);
}
}
