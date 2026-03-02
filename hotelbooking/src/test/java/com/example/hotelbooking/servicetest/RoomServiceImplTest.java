package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.service.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setType("Deluxe");
        room.setPrice(2000.0);
        room.setAvailable(true);
        room.setHotel(hotel);
    }

    @Test
    void createRoom_ShouldSaveRoomSuccessfully() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.createRoom(1L, room);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        assertEquals(hotel, result.getHotel());
        verify(roomRepository).save(room);
    }

    @Test
    void createRoom_ShouldThrow_WhenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> roomService.createRoom(1L, room));

        assertEquals("Hotel not found", ex.getMessage());
    }

    @Test
    void getRoomsByHotel_ShouldReturnList() {
        when(roomRepository.findByHotelId(1L)).thenReturn(List.of(room));

        List<Room> rooms = roomService.getRoomsByHotel(1L);

        assertEquals(1, rooms.size());
        assertEquals("101", rooms.get(0).getRoomNumber());
    }

    @Test
    void updateRoom_ShouldUpdateSuccessfully() {
        Room updatedRoom = new Room();
        updatedRoom.setRoomNumber("102");
        updatedRoom.setType("Suite");
        updatedRoom.setPrice(3000.0);
        updatedRoom.setAvailable(false);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);

        Room result = roomService.updateRoom(1L, updatedRoom);

        assertEquals("102", result.getRoomNumber());
        assertEquals("Suite", result.getType());
        assertEquals(3000.0, result.getPrice());
        assertFalse(result.isAvailable());
        verify(roomRepository).save(room);
    }

    @Test
    void updateRoom_ShouldThrow_WhenRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> roomService.updateRoom(1L, room));

        assertEquals("Room not found", ex.getMessage());
    }

    @Test
    void deleteRoom_ShouldDeleteSuccessfully() {
        when(roomRepository.existsById(1L)).thenReturn(true);

        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoom_ShouldThrow_WhenRoomNotFound() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> roomService.deleteRoom(1L));

        assertEquals("Room not found", ex.getMessage());
    }
}