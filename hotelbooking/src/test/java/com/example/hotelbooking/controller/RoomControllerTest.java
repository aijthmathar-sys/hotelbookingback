package com.example.hotelbooking.controller;

import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setType("DELUXE");
    }

    @Test
    void testCreateRoom() throws Exception {

        when(roomService.createRoom(Mockito.anyLong(), Mockito.any(Room.class)))
                .thenReturn(room);

        mockMvc.perform(post("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(room.getId()))
                .andExpect(jsonPath("$.roomNumber").value(room.getRoomNumber()))
                .andExpect(jsonPath("$.type").value(room.getType()));
    }
}