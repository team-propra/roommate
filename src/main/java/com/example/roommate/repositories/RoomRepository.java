package com.example.roommate.repositories;

import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository {
    private final List<Room> rooms;
    
    public RoomRepository(Collection<Room> rooms){
        //statically rendered
        this.rooms = new ArrayList<>(rooms);
    }
    
    //only temporary to not interrupt the application flow
    public RoomRepository(){
        rooms = new ArrayList<>();
        rooms.add(new Room(UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1"),"4"));
    }

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
