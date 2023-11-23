package com.example.roommate.services;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.values.Item;
import com.example.roommate.repositories.RoomRepository;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Room findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            return roomRepository.findRoomByID(roomID);
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }

    public void saveAll(List<Room> rooms) {
        roomRepository.saveAll(rooms);
    }

    public List<Item> getItems() {
        return roomRepository.findAll().stream()
                .map(Room::getItems)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
