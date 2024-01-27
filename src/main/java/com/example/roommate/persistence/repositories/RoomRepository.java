package com.example.roommate.persistence.repositories;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository implements IRoomRepository {
    private final List<IRoom> rooms;
    
    public RoomRepository(Collection<IRoom> rooms){
        //statically rendered
        this.rooms = new ArrayList<>(rooms);
    }
    
    //only temporary to not interrupt the application flow
    public RoomRepository(){
        rooms = new ArrayList<>();
    }

    public List<IRoom> findAll(){
        return rooms;
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<IRoom> first = rooms.stream().filter(r -> r.getRoomID().equals(roomID)).findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    public void remove(UUID room){
        rooms.stream().filter(r -> r.getRoomID().equals(room)).findFirst().ifPresent(rooms::remove);
    }

    public void add(IRoom room){
        for(int i=0;i<rooms.size();++i){
            if(rooms.get(i).getRoomID()==room.getRoomID()){
                rooms.set(i,room);
                return;
            }
        }
        rooms.add(room);
    }

    public void saveAll(List<? extends IRoom> rooms) {
        rooms.forEach(this::add);
    }
}
