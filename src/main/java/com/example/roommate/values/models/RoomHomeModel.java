package com.example.roommate.values.models;

import com.example.roommate.values.domainValues.ItemName;

import java.util.UUID;

public record RoomHomeModel(UUID roomID, String roomNumber, Iterable<ItemName> itemNameList, String reservedTime) {
}
