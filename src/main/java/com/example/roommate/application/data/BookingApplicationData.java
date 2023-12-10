package com.example.roommate.application.data;

import com.example.roommate.annotations.ApplicationData;
import com.example.roommate.domain.models.entities.BookingDays;
import com.example.roommate.interfaces.entities.IBooking;

import java.util.UUID;

@ApplicationData
public record BookingApplicationData(UUID roomID, BookingDays bookingDays) implements IBooking {
    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public BookingDays getBookingDays() {
        return bookingDays;
    }


}
