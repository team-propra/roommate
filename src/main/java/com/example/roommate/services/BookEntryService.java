package com.example.roommate.services;

import com.example.roommate.domain.models.entities.BookingEntity;
import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.models.values.BookDataForm;
import com.example.roommate.interfaces.IBookEntryRepository;
import com.example.roommate.repositories.BookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookEntryService {
    IBookEntryRepository bookEntryRepository;

    public BookEntryService(IBookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<BookingEntity> getBookEntries() {
        return bookEntryRepository.getBookDataFormList();
    }
    public void addBookEntry(BookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        BookingEntity bookDataEntry = new BookingEntity(UUID.fromString(form.roomID()), form.Monday19());
        
        if (!bookDataEntry.validateBookingCoorectness()) 
            throw new GeneralDomainException();
        bookEntryRepository.addBookEntry(bookDataEntry);
    }

    


}
