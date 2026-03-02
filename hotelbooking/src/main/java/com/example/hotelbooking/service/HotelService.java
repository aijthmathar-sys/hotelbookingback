package com.example.hotelbooking.service;

import com.example.hotelbooking.entity.Hotel;
import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    List<Hotel> getHotelsByCity(String city);
    Hotel updateHotel(Long id,Hotel updateHotel);
    void deleteHotel(Long id);
}