package com.example.hotelbooking.service;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepository.findByCityIgnoreCase(city);
    }

    @Override
    public Hotel updateHotel(Long id, Hotel updatedHotel) {

        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        existingHotel.setName(updatedHotel.getName());
        existingHotel.setCity(updatedHotel.getCity());
        existingHotel.setAddress(updatedHotel.getAddress());
        existingHotel.setDescription(updatedHotel.getDescription());

        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel(Long id) {

        if (!hotelRepository.existsById(id)) {
            throw new RuntimeException("Hotel not found");
        }

        hotelRepository.deleteById(id);
    }
}


