package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.service.HotelServiceImpl;

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
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Grand Hotel");
        hotel.setCity("Chennai");
        hotel.setAddress("Main Street");
        hotel.setDescription("Luxury stay");
    }

    // ✅ Create Hotel
    @Test
    void createHotel_ShouldSaveHotel() {

        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel saved = hotelService.createHotel(hotel);

        assertEquals("Grand Hotel", saved.getName());
        verify(hotelRepository).save(hotel);
    }

    // ✅ Get All Hotels
    @Test
    void getAllHotels_ShouldReturnList() {

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        List<Hotel> hotels = hotelService.getAllHotels();

        assertEquals(1, hotels.size());
        verify(hotelRepository).findAll();
    }

    // ✅ Get Hotels By City
    @Test
    void getHotelsByCity_ShouldReturnFilteredList() {

        when(hotelRepository.findByCityIgnoreCase("Chennai"))
                .thenReturn(List.of(hotel));

        List<Hotel> hotels = hotelService.getHotelsByCity("Chennai");

        assertEquals(1, hotels.size());
        assertEquals("Chennai", hotels.get(0).getCity());
    }

    // ✅ Update Hotel Success
    @Test
    void updateHotel_ShouldUpdateSuccessfully() {

        Hotel updated = new Hotel();
        updated.setName("Updated Hotel");
        updated.setCity("Mumbai");
        updated.setAddress("New Address");
        updated.setDescription("Updated Desc");

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(hotelRepository.save(any(Hotel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Hotel result = hotelService.updateHotel(1L, updated);

        assertEquals("Updated Hotel", result.getName());
        assertEquals("Mumbai", result.getCity());
        verify(hotelRepository).save(hotel);
    }

    // ❌ Update Hotel Not Found
    @Test
    void updateHotel_ShouldThrow_WhenNotFound() {

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> hotelService.updateHotel(1L, hotel));

        assertEquals("Hotel not found", ex.getMessage());
    }

    // ✅ Delete Hotel Success
    @Test
    void deleteHotel_ShouldDeleteSuccessfully() {

        when(hotelRepository.existsById(1L))
                .thenReturn(true);

        hotelService.deleteHotel(1L);

        verify(hotelRepository).deleteById(1L);
    }

    // ❌ Delete Hotel Not Found
    @Test
    void deleteHotel_ShouldThrow_WhenNotFound() {

        when(hotelRepository.existsById(1L))
                .thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> hotelService.deleteHotel(1L));

        assertEquals("Hotel not found", ex.getMessage());
    }
}