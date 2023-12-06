package com.example.roommate.domain.models.entities;

import java.util.UUID;

public record Booking(UUID roomID, BookingDays bookingDays) {

   /* public boolean validateBookingCoorectness() {
        //At least one box is set to true
        return Monday19;
    }*/
}
