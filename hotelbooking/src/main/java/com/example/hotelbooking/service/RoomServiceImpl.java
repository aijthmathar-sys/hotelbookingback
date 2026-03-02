package com.example.hotelbooking.service;

import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public Room createRoom(Long hotelId, Room room) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        room.setHotel(hotel);

        return roomRepository.save(room);
    }

    @Override
    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public Room updateRoom(Long id, Room updatedRoom) {

        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        existing.setRoomNumber(updatedRoom.getRoomNumber());
        existing.setType(updatedRoom.getType());
        existing.setPrice(updatedRoom.getPrice());
        existing.setAvailable(updatedRoom.isAvailable());

        return roomRepository.save(existing);
    }

    @Override
    public void deleteRoom(Long id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);
    }
}
