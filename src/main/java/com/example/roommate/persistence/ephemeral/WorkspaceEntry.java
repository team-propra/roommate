package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record WorkspaceEntry(UUID id, int workspaceNumber, Iterable<ItemName> itemNames, Iterable<BookedTimeframe> bookedTimeframes) implements IWorkspace {
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
        return IterableSupport.toList(itemNames);
    }

    @Override
    public Iterable<BookedTimeframe> getBookedTimeframes() {
        return IterableSupport.toList(bookedTimeframes);
    }
}
