package com.example.hotelbooking.controller;

import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.service.AvailabilityService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCheckAvailability() throws Exception {

        when(availabilityService.isRoomAvailable(
                Mockito.anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
        )).thenReturn(true);

        mockMvc.perform(get("/api/availability/room")
                        .param("roomId", "1")
                        .param("checkIn", "2026-03-10")
                        .param("checkOut", "2026-03-12"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testSearchAvailableRooms() throws Exception {

        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");

        when(availabilityService.getAvailableRooms(
                Mockito.anyString(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
        )).thenReturn(List.of(room));

        mockMvc.perform(get("/api/availability/search")
                        .param("city", "Chennai")
                        .param("checkIn", "2026-03-10")
                        .param("checkOut", "2026-03-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roomNumber").value("101"));
    }
}