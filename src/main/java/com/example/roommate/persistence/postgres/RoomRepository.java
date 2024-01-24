package com.example.roommate.persistence.postgres;

import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<IRoom> findAll() {
        Iterable<RoomDTO> rooms = roomDAO.findAll();

        List<ItemToRoomDTO> itemToRoomList = itemToRoomDAO.findAll();

        for (RoomDTO room : rooms) {
            List<ItemToRoomDTO> list = itemToRoomList.stream().filter(itemMapEntry -> itemMapEntry.roomID().equals(room.roomID())).toList();

        }
        return StreamSupport.stream(all.spliterator(), false)
                .map(room -> new RoomOOP(room.roomID(), room.roomNumber(),room.itemList(), room.bookedTimeframeList()))
                // map other fields if needed
                .toList();
                return null;
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
    public void saveAll(List<IRoom> rooms) {

    }
}
