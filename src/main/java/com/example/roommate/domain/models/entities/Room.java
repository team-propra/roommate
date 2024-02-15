package com.example.roommate.domain.models.entities;


import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final RoomNumber roomNumber;
    private final List<BookedTimeframe> bookedPeriods;
    private final List<ItemName> itemNameList;

    public Room(UUID roomID, RoomNumber roomNumber, List<BookedTimeframe> bookedPeriods, List<ItemName> itemNameList) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.bookedPeriods = new ArrayList<>(bookedPeriods);
        this.itemNameList = new ArrayList<>(itemNameList);
    }

    public Room(UUID roomID, RoomNumber roomNumber) {
        this(roomID,roomNumber,new ArrayList<>(),new ArrayList<>());
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

    public void addItem(ItemName item) {
        itemNameList.add(item);
    }

    public void addItem(List<ItemName> items) {
        itemNameList.addAll(items);
    }

    public List<ItemName> getItemNames() {
        return itemNameList.stream().toList();
    }

    public void addBookedTimeframe(BookedTimeframe bookedTimeframe) {
        bookedPeriods.add(bookedTimeframe);
    }

    @Override
    public List<BookedTimeframe> getBookdTimeframes() {
        return bookedPeriods.stream().toList();
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
