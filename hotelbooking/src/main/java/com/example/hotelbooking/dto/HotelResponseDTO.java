package com.example.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {

    private Long hotelId;
    private String name;
    private String city;
    private String address;

    
}
