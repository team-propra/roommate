package com.example.roommate.persistence.data;

import com.example.roommate.interfaces.entities.IRoom;

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
}