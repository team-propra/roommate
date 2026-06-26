package com.example.roommate.values.models;

import java.util.List;
import java.util.UUID;

public record RoomOverviewModel(UUID roomID, String roomNumber, List<WorkspaceOverviewModel> workspaces) {
}
