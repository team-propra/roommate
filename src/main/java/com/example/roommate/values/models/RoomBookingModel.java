package com.example.roommate.values.models;

import com.example.roommate.values.domainValues.ItemName;

import java.util.UUID;

public record RoomBookingModel(UUID roomId, String roomNumber, Iterable<ItemName> itemNameList) {
}
