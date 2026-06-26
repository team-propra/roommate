package com.example.roommate.values.models;

import java.util.List;
import java.util.UUID;

public record WorkspaceDetailsModel(
        UUID roomID,
        String roomNumber,
        UUID workspaceID,
        int workspaceNumber,
        List<String> selectedItems,
        List<String> notSelectedItems,
        BookingFrameModel frame
) {
    public WorkspaceDetailsModel {
        selectedItems = List.copyOf(selectedItems);
        notSelectedItems = List.copyOf(notSelectedItems);
    }
}
