package com.example.roommate.services;

import com.example.roommate.domain.entities.BookingEntity;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.BookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEntryService {
    BookEntryRepository bookEntryRepository;

    public BookEntryService(BookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<BookingEntity> getBookEntries() {
        return bookEntryRepository.getBookDataFormList();
    }
    public void addBookEntry(BookDataForm form) throws DomainErrorException {
        BookingEntity bookDataEntry = new BookingEntity(form.roomID(), form.Monday19());
        BookingEntity bookingEntity = new BookingEntity(bookDataEntry.roomID(),bookDataEntry.Monday19());
        if (bookingEntity.validateBookingCoorectness()) {
            bookEntryRepository.addBookEntry(bookDataEntry);
            return;
        }
        throw new DomainErrorException();

    }

    public class DomainErrorException extends Exception{

    }


}
