package com.example.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDTO {

    private Long roomId;
    private String roomNumber;
    private String hotelName;
    private String city;
    private Double price;

    
}
