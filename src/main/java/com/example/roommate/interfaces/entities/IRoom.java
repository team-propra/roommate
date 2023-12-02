package com.example.roommate.interfaces.entities;

import com.example.roommate.interfaces.values.ItemName;

import java.util.List;
import java.util.UUID;

public interface IRoom {
    UUID getRoomID();
    String getRoomNumber();
    List<ItemName> getItems();
}
