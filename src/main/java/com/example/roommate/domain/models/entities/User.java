package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;
import java.util.Set;

public class User implements IUser {
    private UUID keyId;
    private String handle;
    private Set<String> roles;
    private String keyMasterName;

    public User(UUID keyId, String handle, String role) {
        this(keyId, handle, Set.of(role));
    }

    public User(UUID keyId, String handle, Set<String> roles) {
        this.keyId = keyId;
        this.handle = handle;
        this.roles = Set.copyOf(roles);
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

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String getKeyMasterName() {
        return keyMasterName;
    }

    public void setRoles(Set<String> roles) {
        this.roles = Set.copyOf(roles);
    }
}
