package com.example.roommate.domain.models.entities;


import java.util.UUID;

public class Admin extends User {
    public Admin(UUID keyId, String gitHubHandle) {
        super(keyId, gitHubHandle, "ADMIN");
    }
}
