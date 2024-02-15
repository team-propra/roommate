package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID, RoomNumber roomnumber, Iterable<BookedTimeframe> bookedTimeframes, Iterable<? extends IWorkspace> workspaces) implements IRoom {

    public RoomEntry(IRoom room) {
        this(room.getRoomID(),room.getRoomNumber(),room.getBookedTimeframes(),room.getWorkspaces());
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
    public List<BookedTimeframe> getBookedTimeframes() {
        return IterableSupport.toList(bookedTimeframes);
    }

    @Override
    public Iterable<? extends IWorkspace> getWorkspaces() {
        return IterableSupport.toList(workspaces);
    }


}