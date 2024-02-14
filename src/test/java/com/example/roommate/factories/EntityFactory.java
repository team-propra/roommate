package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.models.entities.*;

import java.util.UUID;


@Factory
public class EntityFactory {
    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        UUID keyId = UUID.fromString("fe060a0b-d410-44a2-901e-11148f986a72");
        return new Admin(keyId, "TheAdminGuy");
    }

    public static Room createRoom() {
        return new Room(id, "12");
    }

    public static User createUser() {
        UUID keyId = UUID.fromString("77f11d9e-6894-402e-8b44-8e096fe91d39");
        return new User(keyId, "TheUserGuy", "USER");
    }


}

