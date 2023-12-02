package com.example.roommate.domain.models.entities;


import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.domain.values.ItemName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final String roomnumber;

    private final List<ItemName> itemNameList = new ArrayList<>();

    public Room(UUID roomID, String roomnumber) {
        this.roomID = roomID;
        this.roomnumber = roomnumber;
    }

    public UUID getRoomID() {
        return roomID;
    }

    public String getRoomNumber() {
        return roomnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomID, room.roomID) && Objects.equals(roomnumber, room.roomnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, roomnumber);
    }

    public void addItem(ItemName item) {
        itemNameList.add(item);
    }

    public void addItem(List<ItemName> items) {
        itemNameList.addAll(items);
    }

    public List<ItemName> getItems() {
        return itemNameList;
    }
}
