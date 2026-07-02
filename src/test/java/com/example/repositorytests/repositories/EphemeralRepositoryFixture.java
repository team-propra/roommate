package com.example.repositorytests.repositories;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.interfaces.repositories.IUserRepository;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.persistence.ephemeral.UserRepository;

final class EphemeralRepositoryFixture implements RepositoryFixture {
    private final RoomRepository rooms = new RoomRepository();
    private final ItemRepository items = new ItemRepository();
    private final UserRepository users = new UserRepository();

    @Override
    public RepositoryBackend backend() {
        return RepositoryBackend.EPHEMERAL;
    }

    @Override
    public IRoomRepository rooms() {
        return rooms;
    }

    @Override
    public IItemRepository items() {
        return items;
    }

    @Override
    public IUserRepository users() {
        return users;
    }
}
