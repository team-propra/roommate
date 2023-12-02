package com.example.roommate.tests.services;


import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.tests.factories.ServiceFactory;
import com.example.roommate.tests.factories.ValuesFactory;
import com.example.roommate.interfaces.exceptions.GeneralDomainException;
import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.services.BookingApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
public class BookingApplicationServiceTest {

    @DisplayName("BookDataForm can be added to BookEntryService")
    @Test
    void test_1() throws GeneralDomainException {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        BookDataForm validBookDataForm = ValuesFactory.createBookDataForm();

        bookingApplicationService.addBookEntry(validBookDataForm);

        UUID id = ValuesFactory.id;

        //assert: look if the id is contained in the bookEntryService
        List<UUID> ids = bookingApplicationService.getBookEntries().stream().map(IBooking::getRoomID).toList();
        assertThat(ids).contains(id);

    }

    @DisplayName("adding a invalid bookDataForm results in a GeneralDomainException")
    @Test
    void test_2() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        BookDataForm invalidBookDataForm = ValuesFactory.createInvalidBookDataForm();

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
}
