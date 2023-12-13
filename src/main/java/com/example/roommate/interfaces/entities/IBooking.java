package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;
import com.example.roommate.values.domain.BookingDays;

import java.util.UUID;

@Interface
public interface IBooking {
    UUID getRoomID();
    BookingDays getBookingDays();
}
