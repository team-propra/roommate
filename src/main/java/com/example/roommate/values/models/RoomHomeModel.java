package com.example.roommate.values.models;

import com.example.roommate.interfaces.entities.IWorkspace;

import java.util.List;
import java.util.UUID;

public record RoomHomeModel(UUID roomID, String roomNumber,  String reservedTime, List<? extends IWorkspace> workspaces) {
}
