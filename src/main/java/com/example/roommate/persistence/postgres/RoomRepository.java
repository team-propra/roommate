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



    public IRoom convertRoomDTO(RoomDTO roomDTO){

        return null;
    }
    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        return null;
    }

    @Override
    public void remove(UUID room) {

    }

    @Override
    public void add(IRoom room) {

    }

    @Override
    public void saveAll(List<? extends IRoom> rooms) {

    }
}
