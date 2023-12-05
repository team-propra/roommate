package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.values.ItemName;
import com.example.roommate.exceptions.NotFoundRepositoryException;

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
    }

    public void addRoom(IRoom room) {
        roomRepository.save(room);
    }

    public void removeRoom(IRoom room) {roomRepository.remove(room.getRoomID());}

    public List<IRoom> getRooms() {
        return roomRepository.findAll();
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
