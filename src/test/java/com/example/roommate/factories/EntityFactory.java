package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.models.entities.*;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;


@Factory
public class EntityFactory {
    public static final UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        UUID keyId = UUID.fromString("fe060a0b-d410-44a2-901e-11148f986a72");
        return new Admin(keyId, "TheAdminGuy");
    }

    public static Room createRoom() {
        return createRoom(new RoomNumber("12"));
    }
    public static Room createRoom(RoomNumber roomNumber) {
        return new Room(id, roomNumber, List.of());
    }

    public static User createUser() {
        UUID keyId = UUID.fromString("77f11d9e-6894-402e-8b44-8e096fe91d39");
        return new User(keyId, "TheUserGuy", "USER");
    }


    public static Workspace createWorkspace() { return new Workspace(id, 14, List.of(),List.of()); }
}

