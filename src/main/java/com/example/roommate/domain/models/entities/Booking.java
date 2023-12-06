package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IBooking;

import java.util.UUID;

public record Booking(UUID roomID, boolean Monday19) implements IBooking {

    public boolean validateBookingCoorectness() {
        //At least one box is set to true
        return Monday19;
    }

    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public boolean getMonday19() {
        return Monday19;
    }
}
