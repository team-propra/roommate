package com.example.roommate.examples;

import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.List;
import java.util.UUID;

public class Officer {
    
    public static ItemName SuperExpensiveChair(){
        return ValuesFactory.createItemName("Super Expensive Chair");
    }

    public static ItemName GlassDesk(){
        return ValuesFactory.createItemName("Glass Desk");
    }
    public static UUID WorkspaceID(){
        return UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
    }
    public static Workspace Workspace(){
        // EntityBuilder
        return new Workspace(WorkspaceID(), 1337, List.of(SuperExpensiveChair(), GlassDesk()), List.of());
    }
    public static UUID RoomID(){
        return UUID.fromString("993e4888-9953-4a52-9d6c-3ab32ae43c7f");
    }
    public static Room Room(){
        return new Room(RoomID(),new RoomNumber("office"), List.of(Workspace()));
    }
}
