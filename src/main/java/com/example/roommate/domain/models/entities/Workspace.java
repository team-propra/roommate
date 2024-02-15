package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.ItemName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Workspace implements IWorkspace {

    UUID id;
    int workspaceNumber;
    ArrayList<ItemName> items;
    public Workspace(UUID id, int workspaceNumber, Iterable<ItemName> items) {
        this.items = new ArrayList<>(IterableSupport.toList(items));
        this.id = id;
        this.workspaceNumber = workspaceNumber;
    }

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

    public void removeItem(ItemName item) {
        items.remove(item);
    }

    public void addItem(ItemName item) {
        items.add(item);
    }

    public void addItem(Iterable<ItemName> items) {
        items.forEach(item->this.items.add(item));
    }
}