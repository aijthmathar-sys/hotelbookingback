package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.BookingRequestDTO;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.service.BookingService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // 🔥 Create Booking
    @PostMapping
    public Booking createBooking(@RequestBody BookingRequestDTO request,
                                 Authentication authentication) {

        String username = authentication.getName();

        return bookingService.createBooking(request, username);
    }

    
    @PutMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id,
                                Authentication authentication) {

        String username = authentication.getName();

        bookingService.cancelBooking(id, username);

        return "Booking cancelled successfully";
    }

    
    @GetMapping("/my")
    public List<Booking> getMyBookings(Authentication authentication) {

        String username = authentication.getName();

        return bookingService.getUserBookings(username);
    }
}