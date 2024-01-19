package com.example.roommate.persistence.data;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID, String roomnumber,List<BookedTimeframe> bookedTimeframes) implements IRoom {

    
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
        return bookedTimeframes;
    }

    @Override
    public boolean isAvailable(BookedTimeframe bookedTimeframe) {
        return bookedTimeframes.stream().noneMatch(timeFrame -> timeFrame.intersects(bookedTimeframe));
    }
}