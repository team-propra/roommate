package com.example.roommate.domain.models.entities;


import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final RoomNumber roomNumber;
    private final List<Workspace> workspaces;

    public Room(UUID roomID, RoomNumber roomNumber, List<? extends IWorkspace> workspaces) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.workspaces = workspaces.stream().map(x->new Workspace(x.getId(),x.getWorkspaceNumber(),x.getItems(),x.getBookedTimeframes())).toList();
    }

    public Room(UUID roomID, RoomNumber roomNumber) {
        this(roomID,roomNumber,new ArrayList<>());
    }
    

    public UUID getRoomID() {
        return roomID;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomID, room.roomID) && Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, roomNumber);
    }



    @Override
    public List<Workspace> getWorkspaces() {
        return workspaces.stream().toList(); // TODO consistency /immutability
    }


    

}
