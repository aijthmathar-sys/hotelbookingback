package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.dto.BookingRequestDTO;
import com.example.hotelbooking.entity.*;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.service.AvailabilityService;
import com.example.hotelbooking.service.BookingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDTO request;
    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        request = new BookingRequestDTO();
        request.setRoomId(1L);
        request.setCheckInDate(LocalDate.of(2026, 2, 10));
        request.setCheckOutDate(LocalDate.of(2026, 2, 15));

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        room = new Room();
        room.setId(1L);
        room.setPrice(1000.0);
    }

    // ✅ SUCCESS CASE
    @Test
    void createBooking_ShouldCreateSuccessfully() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(availabilityService.isRoomAvailable(any(), any(), any()))
                .thenReturn(true);

        when(bookingRepository.existsOverlappingBooking(any(), any(), any()))
                .thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking = bookingService.createBooking(request, "test@example.com");

        assertEquals(5000.0, booking.getTotalPrice());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    // ❌ Invalid Date
    @Test
    void createBooking_ShouldThrow_WhenInvalidDate() {

        request.setCheckOutDate(request.getCheckInDate());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(request, "test@example.com"));

        assertEquals("Invalid date selection", ex.getMessage());
    }

    // ❌ Room Not Available
    @Test
    void createBooking_ShouldThrow_WhenNotAvailable() {

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(any()))
                .thenReturn(Optional.of(room));

        when(availabilityService.isRoomAvailable(any(), any(), any()))
                .thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(request, "test@example.com"));

        assertEquals("Room not available for selected dates", ex.getMessage());
    }

    // ❌ Cancel Booking Wrong User
    @Test
    void cancelBooking_ShouldThrow_WhenWrongUser() {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCheckInDate(LocalDate.now().plusDays(2));

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(1L, "other@example.com"));

        assertEquals("You cannot cancel this booking", ex.getMessage());
    }

    // ✅ Cancel Success
    @Test
    void cancelBooking_ShouldCancelSuccessfully() {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCheckInDate(LocalDate.now().plusDays(5));

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L, "test@example.com");

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    // ✅ Get User Bookings
    @Test
    void getUserBookings_ShouldReturnBookings() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByUserId(1L))
                .thenReturn(List.of(new Booking()));

        List<Booking> bookings =
                bookingService.getUserBookings("test@example.com");

        assertEquals(1, bookings.size());
    }
}