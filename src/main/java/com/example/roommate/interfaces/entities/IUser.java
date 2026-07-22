package com.example.roommate.interfaces.entities;

import com.example.roommate.annotations.Interface;

import java.util.UUID;
import java.util.Set;

@Interface
public interface IUser {
    UUID getKeyId();

    String getHandle();

    Set<String> getRoles();

    String getKeyMasterName();
}
