package com.example.roommate.domain.entities;


import java.util.Objects;
import java.util.UUID;

public class Room {

    public UUID roomID;
    public String roomnumber;

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
}
