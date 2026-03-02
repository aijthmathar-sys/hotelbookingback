package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.BookingRequestDTO;
import com.example.hotelbooking.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingRequestDTO request, String username);

    void cancelBooking(Long bookingId, String username);

    List<Booking> getUserBookings(String username);
}