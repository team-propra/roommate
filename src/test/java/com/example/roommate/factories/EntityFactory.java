package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.domain.models.entities.*;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.values.domainValues.ItemName;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Factory
public class EntityFactory {
    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");

    public static Admin createAdmin() {
        return new Admin();
    }

    public static Booking createBookingEntity() {
        return new Booking(id, BookingDays.createBookingDays(60));
    }

    public static Room createRoom() {
        return new Room(id, "12");
    }

    public static List<IRoom> createRoomsWithItems() {
        Room room1 = new Room(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a"), "12");
        room1.addItem(new ItemName("Chair"));
        Room room2 = new Room(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"), "13");
        room2.addItem(new ItemName("Table"));
        room2.addItem(new ItemName("Desk"));
        return List.of(room1, room2);
    }

    public static Collection<IBooking> createMockedBookings() {
        Booking booking = mock(Booking.class);
        when(booking.getRoomID()).
                thenReturn(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a")).
                thenReturn(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"));
        return List.of(booking);
    }

    public static User createUser() {
        return new User();
    }


}

