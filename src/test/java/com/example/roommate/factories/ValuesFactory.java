package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.persistence.ephemeral.WorkspaceEntry;
import com.example.roommate.values.domainValues.*;
import com.example.roommate.persistence.ephemeral.RoomEntry;
import com.example.roommate.values.forms.BookDataForm;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Factory
public class ValuesFactory {


    public static UUID roomId = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static UUID workspaceId = UUID.fromString("30d2d8d5-ce75-499f-b1fe-904b5f55d2f0");
    public static BookDataForm createValidBookDataForm() {
        return new BookDataForm(workspaceId, roomId, 1);
    }
    public static BookDataForm createInvalidBookDataForm() {//doesnt match new BookDataForm
        return new BookDataForm(workspaceId, roomId, 60);
    }

    public static BookedTimeframe createBookedTimeframe(LocalTime startTime, Duration duration, String userHandle) {
        return new BookedTimeframe(DayOfWeek.MONDAY, startTime, duration, userHandle);
    }
    public static BookedTimeframe createBookedTimeframe(LocalTime startTime, Duration duration) {
        return new BookedTimeframe(DayOfWeek.MONDAY, startTime, duration, "default");
    }

    public static IntermediateBookDataForm createInvalidIntermediateBookDataForm() throws ArgumentValidationException {
        BookDataForm validBookDataForm = createInvalidBookDataForm();
        BookingDays invalid = BookingDays.from(-99999,List.of());
        return new IntermediateBookDataForm(validBookDataForm,invalid);
    }

    public static ItemName createItemName() {
        return new ItemName("Item");
    }

    public static ItemName createItemName(String type) {
        return new ItemName(type);
    }

    public static RoomEntry createRoomEntry() { return new RoomEntry(roomId,new RoomNumber("14"), List.of());}

    public static RoomEntry createRoomEntry(String roomnumber) { return new RoomEntry(roomId,new RoomNumber(roomnumber),List.of());}


    public static WorkspaceEntry createWorkspaceEntry() { return new WorkspaceEntry(roomId, 14, List.of(),List.of()); }
}
