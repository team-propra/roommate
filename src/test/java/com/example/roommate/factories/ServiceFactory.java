package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.persistence.repositories.BookEntryRepository;
import com.example.roommate.persistence.repositories.ItemRepository;
import com.example.roommate.persistence.repositories.RoomRepository;
import com.example.roommate.services.BookingApplicationService;
import com.example.roommate.services.LoginApplicationService;
import com.example.roommate.domain.services.RoomDomainService;

@Factory
public class ServiceFactory {
    public static RoomDomainService createRoomService(RoomRepository roomRepository, ItemRepository itemRepository) {
        return new RoomDomainService(roomRepository, itemRepository);
    }
    
    public static LoginApplicationService createLoginService() {
        return new LoginApplicationService();
    }
    
    public static BookingApplicationService createBookingService() {
        return new BookingApplicationService(new BookEntryDomainService(new BookEntryRepository()));
    }
}
