package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;

import java.util.UUID;

@Interface
public interface IBooking {
    UUID getRoomID();
    boolean getMonday19();
}
