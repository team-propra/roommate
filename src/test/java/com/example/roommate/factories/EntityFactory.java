package com.example.roommate.factories;

import com.example.roommate.domain.models.entities.Admin;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;

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

