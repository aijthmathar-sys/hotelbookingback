package com.example.hotelbooking.controller;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateHotel() throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Taj");
        hotel.setCity("Chennai");

        when(hotelService.createHotel(Mockito.any(Hotel.class)))
                .thenReturn(hotel);

        mockMvc.perform(post("/api/hotels")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAllHotels() throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Taj");
        hotel.setCity("Chennai");

        when(hotelService.getAllHotels())
                .thenReturn(List.of(hotel));

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Taj"));
    }

    @Test
    void testGetHotelsByCity() throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Taj");
        hotel.setCity("Chennai");

        when(hotelService.getHotelsByCity("Chennai"))
                .thenReturn(List.of(hotel));

        mockMvc.perform(get("/api/hotels/city/Chennai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Chennai"));
    }

    @Test
    void testUpdateHotel() throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Updated Taj");
        hotel.setCity("Chennai");

        when(hotelService.updateHotel(Mockito.eq(1L), Mockito.any(Hotel.class)))
                .thenReturn(hotel);

        mockMvc.perform(put("/api/hotels/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Taj"));
    }

    @Test
    void testDeleteHotel() throws Exception {

        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hotel deleted successfully"));
    }
}
