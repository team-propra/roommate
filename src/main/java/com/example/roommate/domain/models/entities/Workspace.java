package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record Workspace(UUID id, int workspaceNumber, List<ItemName> items) implements IWorkspace {
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int getWorkspaceNumber() {
        return workspaceNumber;
    }

    @Override
    public List<ItemName> getItems() {
        return items.stream().toList();
    }
}