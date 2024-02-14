package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID, String roomnumber, Iterable<BookedTimeframe> bookedTimeframes) implements IRoom {

    
    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public String getRoomNumber() {
        return roomnumber;
    }

    @Override
    public List<ItemName> getItemNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BookedTimeframe> getBookedTimeframes() {
        return IterableSupport.toList(bookedTimeframes);
    }



}