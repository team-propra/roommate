package com.example.roommate.domain;

import com.example.roommate.domain.models.entities.Admin;
import com.example.roommate.domain.models.entities.BookingEntity;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.factories.EntityFactory;
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

