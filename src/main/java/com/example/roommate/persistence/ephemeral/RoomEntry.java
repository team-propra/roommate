package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.UUID;

public record RoomEntry(UUID roomID, RoomNumber roomnumber, Iterable<? extends IWorkspace> workspaces) implements IRoom {

    public RoomEntry(IRoom room) {
        this(room.getRoomID(),room.getRoomNumber(),room.getWorkspaces());
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
    public Iterable<? extends IWorkspace> getWorkspaces() {
        return IterableSupport.toList(workspaces);
    }


}