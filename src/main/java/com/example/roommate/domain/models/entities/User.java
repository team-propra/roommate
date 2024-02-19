package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

public class User implements IUser {
    private UUID keyId;
    private String handle;
    private String role;
    private String keyMasterName;

    public User(UUID keyId, String handle, String role) {
        this.keyId = keyId;
        this.handle = handle;
        this.role = role;
    }
    public User(UUID keyId, String handle, String role, String keyMasterName) {
        this(keyId, handle, role);
        this.keyMasterName = keyMasterName;
    }

    public UUID getKeyId() {
        return keyId;
    }

    public void setKeyId(UUID keyId) {
        this.keyId = keyId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getKeyMasterName() {
        return keyMasterName;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
