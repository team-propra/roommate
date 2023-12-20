package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;
import com.example.roommate.values.domainValues.CalendarDays;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

@Interface
public interface IRoom {
    UUID getRoomID();
    String getRoomNumber();
    List<ItemName> getItemNames();

    CalendarDays getCalendarDays();
}
