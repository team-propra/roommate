package com.example.roommate.domain.entities;


import com.example.roommate.domain.values.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room {

    private UUID roomID;
    private String roomnumber;

    private final List<Item> itemList = new ArrayList<>();

    public Room(UUID roomID, String roomnumber) {
        this.roomID = roomID;
        this.roomnumber = roomnumber;
    }

    public UUID getRoomID() {
        return roomID;
    }

    public String getRoomnumber() {
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

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void addItem(List<Item> items) {
        items.forEach(item -> itemList.add(item));
    }

    public List<Item> getItems() {
        return itemList;
    }
}
