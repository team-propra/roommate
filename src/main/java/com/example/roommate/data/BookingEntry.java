package com.example.roommate.data;

import com.example.roommate.domain.models.entities.BookingDays;

import java.util.UUID;

public record BookingEntry(UUID roomID, BookingDays bookingDays){}