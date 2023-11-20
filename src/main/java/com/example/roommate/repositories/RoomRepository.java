package com.example.roommate.repositories;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoomRepository {
    private final List<Room> rooms = new ArrayList<>();
    

    public List<Room> findAll(){
        return rooms;
    }

    public Room findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<Room> first = rooms.stream().filter(r -> r.getRoomID().equals(roomID)).findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    public void remove(Room room){
        rooms.remove(room);
    }

    public void save(Room room){
        for(int i=0;i<rooms.size();++i){
            if(rooms.get(i).getRoomID()==room.getRoomID()){
                rooms.set(i,room);
                return;
            }
        }
        rooms.add(room);
    }

    public void saveAll(List<Room> rooms) {
        rooms.forEach(this::save);
    }
}
