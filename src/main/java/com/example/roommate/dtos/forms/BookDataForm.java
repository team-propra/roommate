package com.example.roommate.dtos.forms;


import com.example.roommate.domain.models.entities.BookingDays;
import com.example.roommate.validator.IsValidUUID;

import java.util.List;


public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/ BookingDays bookingDays){}
