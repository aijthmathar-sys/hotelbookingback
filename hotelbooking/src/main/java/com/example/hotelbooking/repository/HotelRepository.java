package com.example.hotelbooking.repository;

import com.example.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByCityIgnoreCase(String city);
}