package com.example.roommate.tests.domain;

import com.example.roommate.domain.entities.Admin;
import com.example.roommate.domain.entities.BookingEntity;
import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.entities.User;
import com.example.roommate.tests.factories.EntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        BookingEntity bookingEntity = EntityFactory.createBookingEntity();
        boolean validated = bookingEntity.validateBookingCoorectness();
        assertThat(validated).isTrue();
    }

    @DisplayName("Room has correct room number")
    @Test
    void test_3() {
        Room room = EntityFactory.createRoom();
        String roomNumber = room.getRoomnumber();
        assertThat(roomNumber).isEqualTo("12");
    }

    @DisplayName("Can create a User")
    @Test
    void test_4() {
        User user = EntityFactory.createUser();
        assertThat(user).isInstanceOf(User.class);
    }
}

