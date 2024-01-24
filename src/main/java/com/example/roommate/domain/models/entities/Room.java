package com.example.roommate.domain.models.entities;


import com.example.roommate.interfaces.entities.IRoom;
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

    public Room(UUID roomID, String roomNumber, List<BookedTimeframe> bookedPeriods, List<ItemName> itemNameList) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.bookedPeriods = bookedPeriods;
        this.itemNameList = itemNameList;
    }

    public Room(UUID roomID, String roomNumber) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.bookedPeriods = new ArrayList<>();
        this.itemNameList = new ArrayList<>();
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

    @Override
    public List<BookedTimeframe> getBookedTimeframes() {
        return bookedPeriods;
    }

}
