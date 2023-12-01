package com.example.roommate.services;

import com.example.roommate.data.RoomEntry;
import com.example.roommate.persistence.ItemRepository;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.values.ItemName;
import com.example.roommate.persistence.RoomRepository;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomService {

    RoomRepository roomRepository;
    ItemRepository itemRepository;

    public RoomService(RoomRepository roomRepository, ItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
    }

    public void addRoom(Room room) {
        roomRepository.save(new RoomEntry(room.getRoomID(), room.getRoomnumber()));
    }

    public void removeRoom(Room room) {roomRepository.remove(room.getRoomID());}

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
        roomRepository.saveAll(rooms.stream().map(r -> new RoomEntry(r.getRoomID(), r.getRoomnumber())).toList());
    }

    public List<ItemName> getItems() {
        return itemRepository.getItems();
        /*return roomRepository.findAll().stream()
                .map(roomEntry -> new Room(roomEntry.roomID(), roomEntry.roomnumber()))
                .map(Room::getItems)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());*/
    }

}
