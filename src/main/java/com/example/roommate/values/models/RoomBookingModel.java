package com.example.roommate.values.models;

import com.example.roommate.values.domainValues.ItemName;

import java.util.UUID;

public record RoomBookingModel(UUID roomID, UUID workspaceID, int workspaceNumber, String roomNumber, Iterable<ItemName> itemNameList) {
}
