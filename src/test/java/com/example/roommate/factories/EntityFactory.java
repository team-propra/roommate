package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.models.entities.*;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.ArrayList;
import java.util.UUID;


@Factory
public class EntityFactory {
    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        return new Admin();
    }

    public static Room createRoom() {
        return new Room(id, new RoomNumber("12"), new ArrayList<>(), new ArrayList<>());
    }

    public static User createUser() {
        return new User();
    }


}

