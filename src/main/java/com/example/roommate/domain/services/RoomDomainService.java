package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.values.ItemName;
import com.example.roommate.exceptions.NotFoundRepositoryException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DomainService
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomDomainService {

    IRoomRepository roomRepository;
    IItemRepository itemRepository;

    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
        Room room1 = new Room(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a"), "12");
        room1.addItem(new ItemName("Chair"));
        Room room2 = new Room(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"), "13");
        room2.addItem(new ItemName("Table"));
        roomRepository.add(room1);
        roomRepository.add(room2);
    }

    public void addRoom(IRoom room) {
        roomRepository.add(room);
    }

    public void removeRoom(IRoom room) {roomRepository.remove(room.getRoomID());}

    public Collection<IRoom> getRooms() {
        return roomRepository.findAll().stream()
                .map(iroom -> (IRoom) new Room(iroom.getRoomID(), iroom.getRoomNumber()))
                .toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            IRoom roomByID = roomRepository.findRoomByID(roomID);
            return new Room(roomByID.getRoomID(), roomByID.getRoomNumber());
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }

    public void saveAll(List<IRoom> rooms) {
        roomRepository.saveAll(rooms);
    }

    public List<ItemName> getItems() {
        return itemRepository.getItems();
    }


}
