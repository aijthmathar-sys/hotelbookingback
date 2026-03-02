package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.service.AvailabilityServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        checkIn = LocalDate.of(2026, 1, 10);
        checkOut = LocalDate.of(2026, 1, 15);
    }

    // ✅ Test isRoomAvailable - No Conflicts
    @Test
    void isRoomAvailable_ShouldReturnTrue_WhenNoConflicts() {

        when(bookingRepository.findConflictingBookings(1L, checkIn, checkOut))
                .thenReturn(List.of());

        boolean result = availabilityService.isRoomAvailable(1L, checkIn, checkOut);

        assertTrue(result);
        verify(bookingRepository).findConflictingBookings(1L, checkIn, checkOut);
    }

    // ❌ Test isRoomAvailable - With Conflicts
    @Test
    void isRoomAvailable_ShouldReturnFalse_WhenConflictsExist() {

        Booking booking = new Booking();

        when(bookingRepository.findConflictingBookings(1L, checkIn, checkOut))
                .thenReturn(List.of(booking));

        boolean result = availabilityService.isRoomAvailable(1L, checkIn, checkOut);

        assertFalse(result);
    }

    // ✅ Test getAvailableRooms
    @Test
    void getAvailableRooms_ShouldReturnOnlyAvailableRooms() {

        Room room1 = new Room();
        room1.setId(1L);

        Room room2 = new Room();
        room2.setId(2L);

        when(roomRepository.findRoomsByCity("Chennai"))
                .thenReturn(List.of(room1, room2));

        // room1 available
        when(bookingRepository.findConflictingBookings(1L, checkIn, checkOut))
                .thenReturn(List.of());

        // room2 not available
        when(bookingRepository.findConflictingBookings(2L, checkIn, checkOut))
                .thenReturn(List.of(new Booking()));

        List<Room> availableRooms =
                availabilityService.getAvailableRooms("Chennai", checkIn, checkOut);

        assertEquals(1, availableRooms.size());
        assertEquals(1L, availableRooms.get(0).getId());
    }
}