package com.example.roommate.domain.models.entities;

import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

public class User implements IUser {
    private UUID keyId;
    private String gitHubHandle;
    private String role;

    public User(UUID keyId, String gitHubHandle, String role) {
        this.keyId = keyId;
        this.gitHubHandle = gitHubHandle;
        this.role = role;
    }

    public UUID getKeyId() {
        return keyId;
    }

    public void setKeyId(UUID keyId) {
        this.keyId = keyId;
    }

    public String getGitHubHandle() {
        return gitHubHandle;
    }

    public void setGitHubHandle(String gitHubHandle) {
        this.gitHubHandle = gitHubHandle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
