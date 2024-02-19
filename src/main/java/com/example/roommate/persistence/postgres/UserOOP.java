package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

public record UserOOP(UUID id, String handle, String role) implements IUser {
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
        return null;
    }
}
