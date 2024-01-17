package com.example.roommate.tests.services;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.repositories.BookEntryRepository;
import com.example.roommate.persistence.repositories.ItemRepository;
import com.example.roommate.persistence.repositories.RoomRepository;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestClass
public class BookingApplicationServiceTest {

    @DisplayName("BookDataForm can be added to BookEntryService")
    @Test
    void test_1() throws GeneralDomainException, Exception {


        RoomDomainService roomDomainService = Mockito.mock(RoomDomainService.class);
        doNothing().when(roomDomainService).addBooking(any(), any());

        //BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        BookingApplicationService bookingApplicationService = new BookingApplicationService(new BookEntryDomainService(new BookEntryRepository()), roomDomainService);
        IntermediateBookDataForm validIntermediateBookDataForm = ValuesFactory.createValidIntermediateBookDataForm();

        bookingApplicationService.addBookEntry(ValuesFactory.createValidIntermediateBookDataForm());

        UUID id = ValuesFactory.id;

        //assert: look if the id is contained in the bookEntryService
        List<UUID> ids = bookingApplicationService.getBookEntries().stream().map(IBooking::getRoomID).toList();
        assertThat(ids).contains(id);

    }

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
