package com.example.roommate.domain.models.entities;


import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final String roomNumber;
    private final List<BookedTimeframe> bookedPeriods;
    private final List<ItemName> itemNameList;
    private final List<Workspace> workspaces;
    
    
    public Room(UUID roomID, String roomNumber, List<BookedTimeframe> bookedPeriods, List<ItemName> itemNameList,List<? extends IWorkspace> workspaces) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.bookedPeriods = bookedPeriods;
        this.itemNameList = itemNameList;
        this.workspaces = workspaces.stream().map(x->new Workspace(x.getId(),x.getWorkspaceNumber(),x.getItems())).toList();
    }

    public Room(UUID roomID, String roomNumber) {
        this(roomID,roomNumber,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }
    

    public UUID getRoomID() {
        return roomID;
    }

    public String getRoomNumber() {
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

    public void addItem(ItemName item) {
        itemNameList.add(item);
    }

    public void addItem(List<ItemName> items) {
        itemNameList.addAll(items);
    }

    public List<ItemName> getItemNames() {
        return itemNameList;
    }

    public void addBookedTimeframe(BookedTimeframe bookedTimeframe) {
        bookedPeriods.add(bookedTimeframe);
    }

    @Override
    public List<BookedTimeframe> getBookdTimeframes() {
        return bookedPeriods;
    }

    @Override
    public List<Workspace> getWorkspaces() {
        return workspaces.stream().toList(); // TODO consistency /immutability
    }


    public boolean isAvailable(BookedTimeframe bookedTimeframe) {
        return bookedPeriods.stream().noneMatch(timeFrame -> timeFrame.intersects(bookedTimeframe));
    }


    public void removeItemName(ItemName itemName) {
        itemNameList.remove(itemName);
    }
    public void addItemName(ItemName itemName) {
        itemNameList.add(itemName);
    }

}
