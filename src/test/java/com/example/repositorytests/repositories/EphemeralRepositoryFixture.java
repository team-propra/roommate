package com.example.repositorytests.repositories;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.interfaces.repositories.IUserRepository;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.persistence.ephemeral.UserRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Repository fixture exposes shared repositories used by default fixture services")
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
