package com.example.hotelbooking.repository;

import com.example.hotelbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);
    @Query("""
SELECT r FROM Room r
WHERE r.hotel.city = :city
""")
List<Room> findRoomsByCity(@Param("city") String city);

}