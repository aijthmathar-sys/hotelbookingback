package com.example.hotelbooking.controller;

import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // ADMIN
    @PostMapping("/{hotelId}")
    public ResponseEntity<Room> createRoom(@PathVariable Long hotelId,
                                           @RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(hotelId, room));
    }

    // Public
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    // ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id,
                                           @RequestBody Room room) {
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}