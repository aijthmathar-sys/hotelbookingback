package com.example.hotelbooking.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long bookingId;
    private String hotelName;
    private String city;
    private String roomNumber;
    private Double price;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    
}
