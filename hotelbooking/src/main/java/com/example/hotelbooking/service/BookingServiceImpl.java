package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.BookingRequestDTO;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.BookingStatus;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    
    private final AvailabilityService availabilityService;

    @Override
    public Booking createBooking(BookingRequestDTO request, String username) {

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new RuntimeException("Invalid date selection");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
                boolean available = availabilityService.isRoomAvailable(
        room.getId(),
        request.getCheckInDate(),
        request.getCheckOutDate()
);

if (!available) {
    throw new RuntimeException("Room not available for selected dates");
}

        boolean isBooked = bookingRepository.existsOverlappingBooking(
                room.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (isBooked) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        double totalPrice = room.getPrice() * days;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId, String username) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(username)) {
            throw new RuntimeException("You cannot cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        if (!booking.getCheckInDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel after check-in date");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getUserBookings(String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserId(user.getId());
    }
}