package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DomainService
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomDomainService {

    public IRoomRepository roomRepository;
    IItemRepository itemRepository;
    RoomDomainService self;

    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
        this.self = this;
    }
    @Autowired
    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository, @Lazy RoomDomainService self) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
        this.self = self;
    }

    public void addDummyDummy(){
        self.addDummieRooms();
    }

    @Transactional
    public void addDummieRooms() {
        ItemName chair = new ItemName("Chair");
        ItemName table = new ItemName("Table");
        ItemName desk = new ItemName("Desk");

        itemRepository.addItem(chair);
        itemRepository.addItem(table);
        itemRepository.addItem(desk);
        Room room1 = new Room(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a"), "12");
        room1.addItem(chair);
        Room room2 = new Room(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"), "13");
        room2.addItem(table);
        room2.addItem(desk);
        roomRepository.add(room1);
        roomRepository.add(room2);
    }


    public void addBooking(BookedTimeframe bookedTimeframe, UUID roomID) throws NotFoundRepositoryException {
        IRoom roomByID = roomRepository.findRoomByID(roomID);
        roomRepository.addBooking(bookedTimeframe,roomByID);
    }


    public void addRoom(RoomApplicationData roomApplicationData){
        roomRepository.add(new Room(roomApplicationData.roomID(), roomApplicationData.roomNumber()));
    }
    

    public void removeRoom(RoomApplicationData room) {roomRepository.remove(room.roomID());}

    public Collection<IRoom> getRooms() {
        return roomRepository.findAll().stream()
                .map(iroom -> (IRoom) new Room(iroom.getRoomID(), iroom.getRoomNumber(),iroom.getBookdTimeframes(),iroom.getItemNames()))
                .toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            return roomRepository.findRoomByID(roomID);
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }


    public List<ItemName> getItems() {
        return itemRepository.getItems();
    }

    public static boolean isRoomAvailable(IRoom room, BookedTimeframe bookedTimeframe){
        return toRoom(room).isAvailable(bookedTimeframe);
    }

    private static Room toRoom(IRoom room){
        return  new Room(room.getRoomID(), room.getRoomNumber(), room.getBookdTimeframes().stream().toList(), room.getItemNames().stream().toList());
    }
    public void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        IRoom iRoom = roomRepository.findRoomByID(roomID);
        Room room = RoomDomainService.toRoom(iRoom);
        room.removeItemName(itemName);
    }

    public void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        IRoom iRoom = roomRepository.findRoomByID(roomID);
        Room room = RoomDomainService.toRoom(iRoom);
        room.addItemName(itemName);
    }

    public void createItem(String itemName) {
        ItemName item = new ItemName(itemName);
        itemRepository.addItem(item);
    }
}
