package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.BookingRequestDTO;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // 🔥 IMPORTANT for LocalDate JSON conversion
    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateBooking() throws Exception {

        BookingRequestDTO request = new BookingRequestDTO();
        request.setRoomId(1L);
        request.setCheckInDate(LocalDate.of(2026, 3, 5));
        request.setCheckOutDate(LocalDate.of(2026, 3, 7));

        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingService.createBooking(
                Mockito.any(BookingRequestDTO.class),
                Mockito.anyString()
        )).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .principal(() -> "test@gmail.com")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testCancelBooking() throws Exception {

        mockMvc.perform(put("/api/bookings/1/cancel")
                        .principal(() -> "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled successfully"));
    }

    @Test
    void testGetMyBookings() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingService.getUserBookings(Mockito.anyString()))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings/my")
                        .principal(() -> "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}