package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomOOP(UUID uuid, String roomNumber, List<ItemName> itemList, List<BookedTimeframe> bookedTimeframeList) implements IRoom {

    @Override
    public UUID getRoomID() {
        return uuid;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public List<ItemName> getItemNames() {
        return itemList;
    }

    @Override
    public List<BookedTimeframe> getBookedTimeframes() {
        return bookedTimeframeList;
    }
}