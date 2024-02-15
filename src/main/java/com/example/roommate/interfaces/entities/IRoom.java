package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.UUID;

@Interface
public interface IRoom {
    UUID getRoomID();
    RoomNumber getRoomNumber();
    Iterable<ItemName> getItemNames();
    Iterable<BookedTimeframe> getBookdTimeframes();
}
