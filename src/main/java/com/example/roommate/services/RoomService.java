package com.example.roommate.services;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomService {

    RoomRepository roomRepository;

    RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    public void removeRoom(Room room) {roomRepository.remove(room);}

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room findRoomByID(UUID roomID) {
        return roomRepository.findRoomByID(roomID);
    }

    public void saveAll(List<Room> rooms) {
        roomRepository.saveAll(rooms);
    }

}
