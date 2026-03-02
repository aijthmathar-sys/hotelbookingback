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
public class BookingRequestDTO {

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;
    private Double totalPrice;
    private Long roomId;

    
}