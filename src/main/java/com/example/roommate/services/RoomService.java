package com.example.roommate.services;

import com.example.roommate.data.RoomEntry;
import com.example.roommate.domain.entities.Room;
import com.example.roommate.persistence.RoomRepository;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomService {

    RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void addRoom(Room room) {
        roomRepository.save(new RoomEntry(room.roomID, room.roomnumber));
    }

    public void removeRoom(Room room) {roomRepository.remove(room.roomID);}

    public List<Room> getRooms() {
        return roomRepository.findAll().stream().map(r -> new Room(r.roomID(), r.roomnumber())).toList();
    }

    public Room findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            RoomEntry roomByID = roomRepository.findRoomByID(roomID);
            return new Room(roomByID.roomID(), roomByID.roomnumber());
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }

    public void saveAll(List<Room> rooms) {
        roomRepository.saveAll(rooms.stream().map(r -> new RoomEntry(r.roomID, r.roomnumber)).toList());
    }

    public List<Item> getItems() {
        return roomRepository.findAll().stream()
                .map(Room::getItems)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
