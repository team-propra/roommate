package com.example.roommate.values.models;

import java.util.List;
import java.util.UUID;

public record WorkspaceOverviewModel(
        UUID workspaceID,
        int workspaceNumber,
        List<String> items,
        List<String> bookedTimeframes
) {
    public WorkspaceOverviewModel {
        items = List.copyOf(items);
        bookedTimeframes = List.copyOf(bookedTimeframes);
    }
}
