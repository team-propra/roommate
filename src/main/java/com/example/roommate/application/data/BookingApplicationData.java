package com.example.roommate.application.data;

import com.example.roommate.annotations.ApplicationData;
import com.example.roommate.interfaces.entities.IBooking;

import java.util.UUID;

@ApplicationData
public record BookingApplicationData(UUID roomID, boolean Monday19) implements IBooking {
    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public boolean getMonday19() {
        return Monday19;
    }
}
