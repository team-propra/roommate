package com.example.roommate.domain.models.entities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record Booking(UUID roomID, BookingDays bookingDays) {



    public  boolean validateBookingCoorectness() {
        //At least one box is set to true
        Stream<List<Boolean>> bookingStream = Stream.of(bookingDays.mondayBookings, bookingDays.tuesdayBookings, bookingDays.wednesdayBookings, bookingDays.thursdayBookings, bookingDays.fridayBookings, bookingDays.saturdayBookings, bookingDays.sundayBookings);
        Stream<Boolean> combinedStream = bookingStream.flatMap(List::stream);
       // stream.forEach(System.out::println);
        return combinedStream.anyMatch(value -> value.equals(true));
    }
}
