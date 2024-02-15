package com.example.roommate.persistence.data;

import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomEntry(UUID roomID, String roomnumber,List<BookedTimeframe> bookedTimeframes, List<? extends IWorkspace> workspaces) implements IRoom {

    
    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public String getRoomNumber() {
        return roomnumber;
    }


    @Override
    public List<BookedTimeframe> getBookedTimeframes() {
        return bookedTimeframes;
    }

    @Override
    public List<? extends IWorkspace> getWorkspaces() {
        return workspaces;
    }


}