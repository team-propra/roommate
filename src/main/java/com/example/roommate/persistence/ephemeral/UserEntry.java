package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

public record UserEntry(UUID id, String handle, String role, String keymasterName) implements IUser {
    public UserEntry(UUID id, String handle, String role) {
        this(id,handle,role, "");
    }

    @Override
    public UUID getKeyId() {
        return id;
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getKeyMasterName() {
        return keymasterName;
    }
}
