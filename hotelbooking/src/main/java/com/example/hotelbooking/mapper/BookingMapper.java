package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.BookingResponseDTO;
import com.example.hotelbooking.entity.Booking;

public class BookingMapper {

    public static BookingResponseDTO toDTO(Booking booking) {

        BookingResponseDTO dto = new BookingResponseDTO();

        dto.setBookingId(booking.getId());
        dto.setHotelName(booking.getRoom().getHotel().getName());
        dto.setCity(booking.getRoom().getHotel().getCity());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setPrice(booking.getRoom().getPrice());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setStatus(booking.getStatus().name());

        return dto;
    }
}