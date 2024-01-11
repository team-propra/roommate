package com.example.roommate.domain.models.entities;

import java.util.List;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.values.domainValues.BookingDays;

import java.util.UUID;
import java.util.stream.Stream;

public record Booking(UUID roomID, BookingDays bookingDays) implements IBooking {


    public  boolean validateBookingCorrectness() {
        //At least one box is set to true
        Stream<List<Boolean>> bookingStream = Stream.of(bookingDays.mondayBookings, bookingDays.tuesdayBookings, bookingDays.wednesdayBookings, bookingDays.thursdayBookings, bookingDays.fridayBookings, bookingDays.saturdayBookings, bookingDays.sundayBookings);
        Stream<Boolean> combinedStream = bookingStream.flatMap(List::stream);
        return combinedStream.anyMatch(value -> value.equals(true));
    }

    @Override
    public UUID getRoomID() {
        return roomID;
    }

    @Override
    public BookingDays getBookingDays() {
        return bookingDays;
    }


}
