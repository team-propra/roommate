package com.example.roommate.tests.factories;

import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.persistence.repositories.BookEntryRepository;
import com.example.roommate.persistence.repositories.ItemRepository;
import com.example.roommate.persistence.repositories.RoomRepository;
import com.example.roommate.services.BookingService;
import com.example.roommate.services.LoginService;
import com.example.roommate.domain.services.RoomDomainService;

public class ServiceFactory {
    public static RoomDomainService createRoomService(RoomRepository roomRepository, ItemRepository itemRepository) {
        return new RoomDomainService(roomRepository, itemRepository);
    }
    
    public static LoginService createLoginService() {
        return new LoginService();
    }
    
    public static BookingService createBookingService() {
        return new BookingService(new BookEntryDomainService(new BookEntryRepository()));
    }
}
