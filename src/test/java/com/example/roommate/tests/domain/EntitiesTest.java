package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Admin;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.values.domain.ItemName;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestClass
public class EntitiesTest {

    @DisplayName("Can create an Admin")
    @Test
    void test_1() {
        Admin admin = EntityFactory.createAdmin();
        assertThat(admin).isInstanceOf(Admin.class);
    }

    @DisplayName("BookingEntity validates Booking correctly")
    @Test
    void test_2() {
        Booking bookingEntity = EntityFactory.createBookingEntity();
        boolean validated = bookingEntity.validateBookingCoorectness();
        assertThat(validated).isTrue();
    }

    @DisplayName("Room has correct room number")
    @Test
    void test_3() {
        Room room = EntityFactory.createRoom();
        String roomNumber = room.getRoomNumber();
        assertThat(roomNumber).isEqualTo("12");
    }

    @DisplayName("Can create a User")
    @Test
    void test_4() {
        User user = EntityFactory.createUser();
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    @DisplayName("add item to room")
    void test_5() {
        Room room = EntityFactory.createRoom();
        ItemName item = ValuesFactory.createItemName("Chair");

        room.addItem(item);

        assertThat(room.getItemNames()).containsExactly(item);
    }

    @Test
    @DisplayName("Add List of Items to a room")
    void test_6() {
        Room room = EntityFactory.createRoom();
        List<ItemName> items = List.of(ValuesFactory.createItemName("Desk"), ValuesFactory.createItemName("Chair"));

        room.addItem(items);

        assertThat(room.getItemNames()).containsExactlyElementsOf(items);
    }

    @Test
    @DisplayName("Get a List of items when getItems() is called")
    void test_7() {
        Room room = EntityFactory.createRoom();
        ItemName desk = ValuesFactory.createItemName("Desk");
        ItemName chair = ValuesFactory.createItemName("Chair");
        room.addItem(chair);
        room.addItem(desk);

        List<ItemName> result = room.getItemNames();

        assertThat(result).containsExactly(chair, desk);
    }
}

