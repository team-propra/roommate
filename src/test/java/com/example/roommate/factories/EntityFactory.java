package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.models.entities.*;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;


@Factory
public class EntityFactory {
    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        return new Admin();
    }

    public static Room createRoom() {
        return new Room(id, new RoomNumber("12"), List.of());
    }

    public static User createUser() {
        return new User();
    }


    public static Workspace createWorkspace() { return new Workspace(id, 14, List.of(),List.of()); }
}

