package com.example.roommate.tests.factories;

import com.example.roommate.domain.entities.Admin;
import com.example.roommate.domain.entities.Booking;
import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.entities.User;

import java.util.UUID;

public class EntityFactory {
    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        return new Admin();
    }

    public static Booking createBookingEntity() {
        return new Booking(id, true);
    }

    public static Room createRoom() {
        return new Room(id, "12");
    }

    public static User createUser() {
        return new User();
    }
}

