package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IUser;

import java.util.Set;
import java.util.UUID;

public record UserOOP(UUID id, String handle, Set<String> roles, String keymasterName) implements IUser {
    public UserOOP {
        roles = Set.copyOf(roles);
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
        return Set.copyOf(roles);
    }

    @Override
    public Set<String> roles() {
        return Set.copyOf(roles);
    }

    @Override
    public String getKeyMasterName() {
        return keymasterName;
    }
}
