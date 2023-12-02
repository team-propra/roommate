package com.example.roommate.persistence.data;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.domain.models.values.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID,String roomnumber) implements IRoom {
    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public String getRoomNumber() {
        return roomnumber;
    }

    @Override
    public List<ItemName> getItems() {
        throw new UnsupportedOperationException();
    }
}