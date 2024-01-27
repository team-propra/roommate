package com.example.roommate.persistence.postgres;

import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class RoomRepository implements IRoomRepository {
    RoomDAO roomDAO;

    ItemDAO itemDAO;

    BookedTimeFrameDAO bookedTimeFrameDAO;

    ItemToRoomDAO itemToRoomDAO;

    public RoomRepository(RoomDAO roomDAO, ItemDAO itemDAO, BookedTimeFrameDAO bookedTimeFrameDAO, ItemToRoomDAO itemToRoomDAO) {
        this.roomDAO = roomDAO;
        this.itemDAO = itemDAO;
        this.bookedTimeFrameDAO = bookedTimeFrameDAO;
        this.itemToRoomDAO = itemToRoomDAO;
    }

    @Override
    public List<? extends IRoom> findAll() {
        // Query
        Iterable<RoomDTO> roomList = roomDAO.findAll();
        List<ItemToRoomDTO> itemToRoomList = IterableSupport.toList(itemToRoomDAO.findAll());
        List<ItemDTO> itemList = IterableSupport.toList(itemDAO.findAll());
        List<BookedTimeframeDTO> book = IterableSupport.toList(bookedTimeFrameDAO.findAll());

        // Map
        List<RoomOOP> result = new ArrayList<>();
        for (RoomDTO room : roomList) {
            List<ItemToRoomDTO> matchingMaps = itemToRoomList.stream()
                    .filter(itemMapEntry -> itemMapEntry.roomID().equals(room.roomID()))
                    .toList();
            List<ItemName> matchingItems = itemList.stream()
                    .filter(item->matchingMaps.stream().anyMatch(
                        map-> map.itemName().equals(item.itemName())
                    ))
                    .map(item-> new ItemName(item.itemName()))
                    .toList();
            List<BookedTimeframe> bookedTimeframes = book.stream()
                    .filter(timeframe -> timeframe.roomID() == room.roomID())
                    .map(timeframe-> new BookedTimeframe(timeframe.day(),timeframe.startTime(),timeframe.duration()))
                    .toList();
            result.add(new RoomOOP(room.roomID(),room.roomNumber(),matchingItems,bookedTimeframes));
        }
        return result;
    }

    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        RoomDTO room = roomDAO.findByRoomID(roomID);
        if(room == null)
            throw new NotFoundRepositoryException();
        List<String> itemToRoomMaps = itemToRoomDAO.findByRoomID(roomID).stream().map(ItemToRoomDTO::itemName).toList();
        List<ItemName> byItemNames = itemDAO.findByItemNames(itemToRoomMaps).stream()
                .map(ItemDTO::toItemName)
                .toList();
        List<BookedTimeframe> timeframes = bookedTimeFrameDAO.findByRoomID(roomID).stream()
                .map(BookedTimeframeDTO::toBookedTimeFrame)
                .toList();
        return new RoomOOP(roomID,room.roomNumber(),byItemNames,timeframes);
    }

    @Override
    public void remove(UUID roomID) {
        roomDAO.deleteById(roomID);
        // TODO delete reference in ItemToRoom using Database Cascade on delete constraint
    }

    @Override
    public void add(IRoom room) {
        RoomDTO dto = addRoom(room);
        roomDAO.save(dto);
    }
    
    private RoomDTO addRoom(IRoom room){
        List<ItemName> itemNames = room.getItemNames();
        itemNames.forEach(x->{
            itemToRoomDAO.save(new ItemToRoomDTO(UUID.randomUUID(),x.type(),room.getRoomID()));
        });
        room.getBookedTimeframes().forEach(x->{
            bookedTimeFrameDAO.save(new BookedTimeframeDTO(UUID.randomUUID(),x.day(),x.startTime(),x.duration(),room.getRoomID()));
        });
        return new RoomDTO(room.getRoomID(),room.getRoomNumber());
    }

    @Override
    public void saveAll(List<? extends IRoom> rooms) {
        List<RoomDTO> roomList = rooms.stream().map(this::addRoom).toList();
        roomDAO.saveAll(roomList);
    }
}
