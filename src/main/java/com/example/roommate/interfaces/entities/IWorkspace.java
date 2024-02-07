package com.example.roommate.interfaces.entities;

import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public interface IWorkspace {
    UUID getId();
    int getWorkspaceNumber();
    List<ItemName> getItems();
}
