package com.example.roommate.domain.entities;


import java.util.UUID;

public class Room {

    public UUID roomID;
    public String roomnumber;

    public Room(UUID roomID, String roomnumber) {
        this.roomID = roomID;
        this.roomnumber = roomnumber;
    }
}
