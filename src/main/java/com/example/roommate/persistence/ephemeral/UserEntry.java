package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IUser;

import java.util.Set;
import java.util.UUID;

public record UserEntry(UUID id, String handle, Set<String> roles, String keymasterName) implements IUser {
    public UserEntry(UUID id, String handle, String role) {
        this(id, handle, Set.of(role), "");
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
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String getKeyMasterName() {
        return keymasterName;
    }
}
