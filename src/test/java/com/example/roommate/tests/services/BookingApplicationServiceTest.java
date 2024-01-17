package com.example.roommate.tests.services;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@TestClass
public class BookingApplicationServiceTest {

    @DisplayName("adding a invalid bookDataForm results in a GeneralDomainException")
    @Test
    void test_2() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        IntermediateBookDataForm invalidBookDataForm = ValuesFactory.createInvalidIntermediateBookDataForm();

        assertThatThrownBy(() -> bookingApplicationService.addBookEntry(invalidBookDataForm)).isInstanceOf(GeneralDomainException.class);

    }


    @DisplayName("adding a null results in a GeneralDomainException")
    @Test
    void test_3() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();

        assertThatThrownBy(() -> {
            bookingApplicationService.addBookEntry(null);
//            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @DisplayName("getRooms() returns a Collection of Rooms")
    @Test
    void test_4() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        Room room = EntityFactory.createRoom();

        bookingApplicationService.addRoom(room);
        Collection<IRoom> rooms = bookingApplicationService.getRooms();

        assertThat(rooms).contains(room);
    }
}
