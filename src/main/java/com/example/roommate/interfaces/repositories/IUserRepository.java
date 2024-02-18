package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;

import java.util.List;
import java.util.UUID;

@RepositoryInterface
public interface IUserRepository {
    void addUser(IUser user);

    void registerKey(UUID keyId, String login);

    IUser getUserByLogin(String login);

    void verifyUser(UUID key, String keymasterName);

    List<User> getAllUser();
}
