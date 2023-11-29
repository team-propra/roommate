package com.example.roommate.tests.factories;

import com.example.roommate.persistence.BookEntryRepository;
import com.example.roommate.persistence.ItemRepository;
import com.example.roommate.persistence.RoomRepository;
import com.example.roommate.services.BookEntryService;
import com.example.roommate.services.LoginService;
import com.example.roommate.services.RoomService;

public class ServiceFactory {
    public static RoomService createRoomService(RoomRepository roomRepository, ItemRepository itemRepository) {
        return new RoomService(roomRepository, itemRepository);
    }
    
    public static LoginService createLoginService() {
        return new LoginService();
    }
    
    public static BookEntryService createBookEntryService(BookEntryRepository entryRepository) {
        return new BookEntryService(entryRepository);
    }
}
