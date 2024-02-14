package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;

import java.util.UUID;

@RepositoryInterface
public interface IUserRepository {
    UUID getKeyId();
    String getGitHubHandle();
    String getRole();
    void addUser(IUser user);
}
