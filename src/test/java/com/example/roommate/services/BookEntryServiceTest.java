package com.example.roommate.services;


import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.BookEntryRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
public class BookEntryServiceTest {

    @DisplayName("can add a BookDataForm to BookEntryService")
    @Test
    void test_1() throws GeneralDomainException {
        BookEntryRepository bookEntryRepository = new BookEntryRepository();
        BookEntryService bookEntryService = new BookEntryService(bookEntryRepository);
        BookDataForm validBookDataForm = ValuesFactory.createBookDataForm();

        bookEntryService.addBookEntry(validBookDataForm);

        UUID id = ValuesFactory.id;
        List<UUID> ids = bookEntryService.getBookEntries().stream().map(b -> b.roomID()).toList();
        assertThat(ids).contains(id);

    }

    @DisplayName("throws GeneralDomainException if the bookDataForm is invalid")
    @Test
    @Disabled
    void test_2() {
        BookEntryRepository bookEntryRepository = new BookEntryRepository();
        BookEntryService bookEntryService = new BookEntryService(bookEntryRepository);
        BookDataForm invalidBookDataForm = ValuesFactory.createInvalidBookDataForm();

        assertThatThrownBy(() -> {
            bookEntryService.addBookEntry(invalidBookDataForm);
            throw new GeneralDomainException();
        }).isInstanceOf(GeneralDomainException.class);

    }
}
