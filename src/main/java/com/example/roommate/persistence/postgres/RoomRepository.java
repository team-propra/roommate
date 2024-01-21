package com.example.roommate.persistence.postgres;

import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IRoomRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class RoomRepository implements IRoomRepository {
    RoomDAO roomDAO;

    public RoomRepository(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public List<IRoom> findAll() {
        /*Iterable<RoomDTO> all = roomDAO.findAll();

        return StreamSupport.stream(all.spliterator(), false)
                .map(room -> new MappedObject(room.getRoomID(), room.getRoomNumber()))
                // map other fields if needed
                .collect(Collectors.toList());;*/
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
