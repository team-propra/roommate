package com.example.roommate.persistence;

import com.example.roommate.data.RoomEntry;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository {
    private final List<RoomEntry> rooms;
    
    public RoomRepository(Collection<RoomEntry> rooms){
        //statically rendered
        this.rooms = new ArrayList<>(rooms);
    }
    
    //only temporary to not interrupt the application flow
    public RoomRepository(){
        rooms = new ArrayList<>();
        rooms.add(new RoomEntry(UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1"),"4"));
    }

    public List<RoomEntry> findAll(){
        return rooms;
    }

    public RoomEntry findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<RoomEntry> first = rooms.stream().filter(r -> r.roomID().equals(roomID)).findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    public void remove(UUID room){
        rooms.stream().filter(r -> r.roomID().equals(room)).findFirst().ifPresent(rooms::remove);
    }

    public void save(RoomEntry room){
        for(int i=0;i<rooms.size();++i){
            if(rooms.get(i).roomID()==room.roomID()){
                rooms.set(i,room);
                return;
            }
        }
        rooms.add(room);
    }

    public void saveAll(List<RoomEntry> rooms) {
        rooms.forEach(this::save);
    }
}
