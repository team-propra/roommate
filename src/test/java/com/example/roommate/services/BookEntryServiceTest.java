package com.example.roommate.services;


import com.example.roommate.domain.models.entities.BookingEntity;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.exceptions.GeneralDomainException;
import com.example.roommate.domain.models.values.BookDataForm;
import com.example.roommate.repositories.BookEntryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
public class BookEntryServiceTest {

    @DisplayName("BookDataForm can be added to BookEntryService")
    @Test
    void test_1() throws GeneralDomainException {
        BookEntryRepository bookEntryRepository = new BookEntryRepository();
        BookEntryService bookEntryService = new BookEntryService(bookEntryRepository);
        BookDataForm validBookDataForm = ValuesFactory.createBookDataForm();

        bookEntryService.addBookEntry(validBookDataForm);

        UUID id = ValuesFactory.id;

        //assert: look if the id is contained in the bookEntryService
        List<UUID> ids = bookEntryService.getBookEntries().stream().map(BookingEntity::roomID).toList();
        assertThat(ids).contains(id);

    }

    @DisplayName("adding a invalid bookDataForm results in a GeneralDomainException")
    @Test
    void test_2() {
        BookEntryRepository bookEntryRepository = new BookEntryRepository();
        BookEntryService bookEntryService = new BookEntryService(bookEntryRepository);
        BookDataForm invalidBookDataForm = ValuesFactory.createInvalidBookDataForm();

        assertThatThrownBy(() -> bookEntryService.addBookEntry(invalidBookDataForm)).isInstanceOf(GeneralDomainException.class);

    }


    @DisplayName("adding a null results in a GeneralDomainException")
    @Test
    void test_3() {
        BookEntryRepository bookEntryRepository = new BookEntryRepository();
        BookEntryService bookEntryService = new BookEntryService(bookEntryRepository);

        assertThatThrownBy(() -> {
            bookEntryService.addBookEntry(null);
//            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class);

    }
}
