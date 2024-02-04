package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.persistence.data.RoomEntry;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;

@Factory
public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createValidBookDataForm() {
        return new BookDataForm(id, 1);
    }
    public static BookDataForm createInvalidBookDataForm() {//doesnt match new BookDataForm
        return new BookDataForm(id, 60);
    }

    public static IntermediateBookDataForm createInvalidIntermediateBookDataForm() {
        BookDataForm validBookDataForm = createInvalidBookDataForm();
        BookingDays invalid = BookingDays.createBookingDays(-99999);
        return new IntermediateBookDataForm(validBookDataForm,invalid);
    }

    public static ItemName createItemName() {
        return new ItemName("Item");
    }

    public static ItemName createItemName(String type) {
        return new ItemName(type);
    }

    public static RoomEntry createRoomEntry() { return new RoomEntry(id,"14", List.of());}

    public static RoomEntry createRoomEntry(String roomnumber) { return new RoomEntry(id,roomnumber,List.of());}
    
    
}
