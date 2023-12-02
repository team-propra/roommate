package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;
import com.example.roommate.domain.models.values.ItemName;

import java.util.List;
import java.util.UUID;

@Interface
public interface IRoom {
    UUID getRoomID();
    String getRoomNumber();
    List<ItemName> getItems();
}
