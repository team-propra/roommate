package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;

public record RoomOOP(UUID uuid, RoomNumber roomNumber, Iterable<ItemName> itemList, Iterable<BookedTimeframe> bookedTimeframes,Iterable<IWorkspace> workspaces) implements IRoom {

    @Override
    public UUID getRoomID() {
        return uuid;
    }

    @Override
    public RoomNumber getRoomNumber() {
        return roomNumber;
    }


    @Override
    public List<BookedTimeframe> getBookedTimeframes() {
        return IterableSupport.toList(bookedTimeframes);    
    }

    @Override
    public List<? extends IWorkspace> getWorkspaces() {
        return IterableSupport.toList(workspaces);
    }
}
