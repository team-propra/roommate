package com.example.roommate.domain.entities;

import java.util.UUID;

public record BookingEntity(UUID roomID, boolean Monday19) {

    public boolean validateBookingCoorectness() {
        return true;
    }
}
