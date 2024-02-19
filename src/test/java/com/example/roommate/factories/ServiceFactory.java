package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.persistence.ephemeral.UserRepository;

@Factory
public class ServiceFactory {
    public static RoomDomainService createRoomService(RoomRepository roomRepository, ItemRepository itemRepository) {
        return new RoomDomainService(roomRepository, itemRepository);
    }

    public static BookingApplicationService createBookingService() {
        return new BookingApplicationService(new RoomDomainService(new RoomRepository(), new ItemRepository()),new UserDomainService(new UserRepository()));
    }
}
