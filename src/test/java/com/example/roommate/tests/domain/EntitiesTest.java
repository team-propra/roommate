package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Admin;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
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


    @DisplayName("Room has correct room number")
    @Test
    void test_3() {
        Room room = EntityFactory.createRoom();
        String roomNumber = room.getRoomNumber().number();
        assertThat(roomNumber).isEqualTo("12");
    }

    @DisplayName("Can create a User")
    @Test
    void test_4() {
        User user = EntityFactory.createUser();
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    @DisplayName("add item to workspace")
    void test_5() {
        Workspace workspace = EntityFactory.createWorkspace();
        ItemName item = ValuesFactory.createItemName("Chair");

        workspace.addItem(item);

        assertThat(workspace.getItems()).containsExactly(item);
    }

    @Test
    @DisplayName("Add List of Items to a workspace")
    void test_6() {
        Workspace workspace = EntityFactory.createWorkspace();
        List<ItemName> items = List.of(ValuesFactory.createItemName("Desk"), ValuesFactory.createItemName("Chair"));

        workspace.addItem(items);

        assertThat(workspace.getItems()).containsExactlyElementsOf(items);
    }

    @Test
    @DisplayName("Get a List of items when getItems() is called")
    void test_7() {
        Workspace workspace = EntityFactory.createWorkspace();
        ItemName desk = ValuesFactory.createItemName("Desk");
        ItemName chair = ValuesFactory.createItemName("Chair");
        workspace.addItem(chair);
        workspace.addItem(desk);

        List<ItemName> result = workspace.getItems();

        assertThat(result).containsExactly(chair, desk);
    }

    @DisplayName("A room is availabel if it is not booked for a timeslot is after current booking")
    @Test
    void test_8() {
        Room room = EntityFactory.createRoom();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(3));
        room.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));

        boolean available = room.isAvailable(desiredSlot);
        assertThat(available).isTrue();
    }

    @DisplayName("A room is availabel if it is not booked for a timeslot is before current booking")
    @Test
    void test_9() {
        Room room = EntityFactory.createRoom();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));
        room.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(3));

        boolean available = room.isAvailable(desiredSlot);
        assertThat(available).isTrue();
    }

    @DisplayName("A room is not availabel if the timeslot before is taken")
    @Test
    void test_10() {
        Room room = EntityFactory.createRoom();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(4));
        room.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));

        boolean available = room.isAvailable(desiredSlot);
        assertThat(available).isFalse();
    }

    @DisplayName("A room is not availabel if the timeslot after is taken")
    @Test
    void test_11() {
        Room room = EntityFactory.createRoom();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));
        room.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(4));

        boolean available = room.isAvailable(desiredSlot);
        assertThat(available).isFalse();
    }

}

