package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

public record WorkspaceOOP(UUID id, int workspaceNumber, Iterable<ItemName> items, Iterable<BookedTimeframe> bookedTimeframes) implements IWorkspace {
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
        return IterableSupport.toList(items);
    }

    @Override
    public Iterable<BookedTimeframe> getBookedTimeframes() {
        return IterableSupport.toList(bookedTimeframes);
    }
}
