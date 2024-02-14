package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID, RoomNumber roomnumber, Iterable<BookedTimeframe> bookedTimeframes, Iterable<ItemName> itemNames) implements IRoom {

    public RoomEntry(IRoom room) {
        this(room.getRoomID(),room.getRoomNumber(),room.getBookdTimeframes(),room.getItemNames());
    }

    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public RoomNumber getRoomNumber() {
        return roomnumber;
    }

    @Override
    public List<ItemName> getItemNames() {
        return IterableSupport.toList(itemNames);
    }

    @Override
    public List<BookedTimeframe> getBookdTimeframes() {
        return IterableSupport.toList(bookedTimeframes);
    }


}