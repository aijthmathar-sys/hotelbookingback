package com.example.hotelbooking.controller;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.service.HotelService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.createHotel(hotel));
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Hotel>> getHotelsByCity(@PathVariable String city) {
        return ResponseEntity.ok(hotelService.getHotelsByCity(city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id,
                                             @RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully");
    }
}