package com.example.roommate.interfaces.entities;

import java.util.UUID;

public interface IUser {
    UUID getKeyId();

    String getGitHubHandle();

    String getRole();

}
