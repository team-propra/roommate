package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.values.domain.BookingDays;
import com.example.roommate.persistence.data.RoomEntry;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.values.domain.ItemName;

import java.util.UUID;

@Factory
public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createBookDataForm() {
        return new BookDataForm(id.toString(), 0, BookingDays.createBookingDays(0));
    }
    public static BookDataForm createInvalidBookDataForm() {//doesnt match new BookDataForm
        return new BookDataForm(id.toString(), 60,BookingDays.createBookingDays(24));
    }

    public static ItemName createItemName() {
        return new ItemName("Item");
    }

    public static ItemName createItemName(String type) {
        return new ItemName(type);
    }

    public static RoomEntry createRoomEntry() { return new RoomEntry(id,"14");}

    public static RoomEntry createRoomEntry(String roomnumber) { return new RoomEntry(id,roomnumber);}
}
