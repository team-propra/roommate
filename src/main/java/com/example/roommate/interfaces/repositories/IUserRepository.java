package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

@RepositoryInterface
public interface IUserRepository {
    void addUser(IUser user);

    void registerKey(UUID keyId, String login);

    IUser getUserByLogin(String login);
}
