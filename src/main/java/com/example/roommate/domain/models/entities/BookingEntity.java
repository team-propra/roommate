package com.example.roommate.domain.models.entities;

import java.util.UUID;

public record BookingEntity(UUID roomID, boolean Monday19) {

    public boolean validateBookingCoorectness() {
        //At least one box is set to true
        return Monday19;
    }
}
