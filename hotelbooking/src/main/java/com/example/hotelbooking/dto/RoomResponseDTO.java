package com.example.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {

    private Long roomId;
    private String roomNumber;
    private String type;
    private Double price;
    private String hotelName;
    private String city;

    // getters & setters
}
