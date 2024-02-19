package com.example.roommate.tests.services;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.exceptions.ArgumentValidationException;
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

@TestClass
public class BookingApplicationServiceTest {

    @DisplayName("adding an invalid bookDataForm results in GeneralDomainException")
    @Test
    void test_2() throws ArgumentValidationException {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        IntermediateBookDataForm invalidBookDataForm = ValuesFactory.createInvalidIntermediateBookDataForm();

        assertThatThrownBy(() -> bookingApplicationService.addBookEntry(invalidBookDataForm, "user")).isInstanceOf(GeneralDomainException.class);

    }


    @DisplayName("adding a null BookEntry results in IllegalArgumentException")
    @Test
    void test_3() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();

        assertThatThrownBy(() -> {
            bookingApplicationService.addBookEntry(null, "user");
//            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @DisplayName("addRoom() yields getRooms() returning a collection of 1 IRoom")
    @Test
    void test_4() throws NotFoundException {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        Room room = EntityFactory.createRoom();

        bookingApplicationService.addRoom(room);
        Collection<IRoom> rooms = bookingApplicationService.getRooms();

        assertThat(rooms).contains(room);
    }
}
