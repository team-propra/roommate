package com.example.roommate.domain.entities;

import java.util.UUID;

public record Booking(UUID roomID, boolean Monday19) {

    public boolean validateBookingCoorectness() {
        //At least one box is set to true
        return Monday19;
    }
}
