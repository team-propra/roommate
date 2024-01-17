package com.example.roommate.values.models;

import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record RoomHomeModel(UUID roomID, String roomNumber, List<ItemName> itemNameList, String reservedTime) {
}
