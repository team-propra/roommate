package com.example.roommate.values.models;

import com.example.roommate.values.domainValues.RoomNumber;

import java.util.UUID;

public record RoomHomeModel(UUID roomID, UUID workspaceID, RoomNumber roomNumber, int workspaceNumber, String reservedTime) {
}
