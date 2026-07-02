package com.example.repositorytests.repositories;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.interfaces.repositories.IUserRepository;

public interface RepositoryFixture extends AutoCloseable {
    RepositoryBackend backend();

    IRoomRepository rooms();

    IItemRepository items();

    IUserRepository users();

    default RoomDomainService roomDomainService() {
        return ServiceFactory.createRoomService(rooms(), items());
    }

    default BookingApplicationService bookingApplicationService() {
        return ServiceFactory.createBookingService(rooms(), items(), users());
    }

    default UserDomainService userDomainService() {
        return new UserDomainService(users());
    }

    @Override
    default void close() {
    }
}
