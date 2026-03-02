package com.example.hotelbooking.repository;

import com.example.hotelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status = 'CONFIRMED'
        AND :checkIn < b.checkOutDate
        AND :checkOut > b.checkInDate
    """)
    boolean existsOverlappingBooking(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );

    List<Booking> findByUserId(Long userId);
    @Query("""
SELECT b FROM Booking b
WHERE b.room.id = :roomId
AND b.status = 'CONFIRMED'
AND (
    :checkIn < b.checkOutDate
    AND :checkOut > b.checkInDate
)
""")
List<Booking> findConflictingBookings(
        @Param("roomId") Long roomId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
);
}