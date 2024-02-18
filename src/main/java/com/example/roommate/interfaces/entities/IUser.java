package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;

import java.util.UUID;

@Interface
public interface IUser {
    UUID getKeyId();

    String getHandle();

    String getRole();

    String getKeyMasterName();
}
