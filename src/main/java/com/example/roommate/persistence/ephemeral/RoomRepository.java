package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("test")
public class RoomRepository implements IRoomRepository {
    private final List<RoomEntry> rooms;
    
    public RoomRepository(Collection<IRoom> rooms){
        //statically rendered
        this.rooms = rooms.stream()
                .map(RoomEntry::new)
                .toList();
    }
    
    //only temporary to not interrupt the application flow
    public RoomRepository(){
        rooms = new ArrayList<>();
    }
    
    public void Overwrite(RoomEntry roomEntry) {
        for(int i=0;i<rooms.size();++i){
            if(rooms.get(i).getRoomID().equals(roomEntry.getRoomID())){
                rooms.set(i,roomEntry);
                return;
            }
        }
    }

    public List<? extends IRoom> findAll(){
        return rooms.stream().toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<RoomEntry> first = rooms.stream().filter(r -> r.getRoomID().equals(roomID)).findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    public void remove(UUID room){
        rooms.stream().filter(r -> r.getRoomID().equals(room)).findFirst().ifPresent(rooms::remove);
    }

    public void add(IRoom room){
        rooms.add(new RoomEntry(room));
    }

    @Override
    public void addBooking(BookedTimeframe bookedTimeframe, IRoom room) {
        List<BookedTimeframe> bookedTimeframes = new ArrayList<>();
        bookedTimeframes.add(bookedTimeframe);
        bookedTimeframes.addAll(IterableSupport.toList(room.getBookdTimeframes()));
        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),bookedTimeframes,room.getItemNames());
        Overwrite(roomEntry);
    }

    @Override
    public void addItem(ItemName itemName, IRoom room) throws NotFoundRepositoryException {
        List<ItemName> itemNames = new ArrayList<>();
        itemNames.add(itemName);
        itemNames.addAll(IterableSupport.toList(room.getItemNames()));
        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),room.getBookdTimeframes(),itemNames);
        Overwrite(roomEntry);
    }

    @Override
    public void removeItem(ItemName itemName, IRoom room) {
        List<ItemName> oldItemsWithoutItemName = IterableSupport.toList(room.getItemNames()).stream().filter(x-> !x.type().equals(itemName.type())).toList();
        List<ItemName> itemNames = new ArrayList<>(oldItemsWithoutItemName);
        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),room.getBookdTimeframes(),itemNames);
        Overwrite(roomEntry);
    }

}
