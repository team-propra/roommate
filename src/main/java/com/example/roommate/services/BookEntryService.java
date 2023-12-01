package com.example.roommate.services;

import com.example.roommate.persistence.data.BookingEntry;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.domain.models.exceptions.GeneralDomainException;
import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.persistence.BookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookEntryService {
    BookEntryRepository bookEntryRepository;

    public BookEntryService(BookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<Booking> getBookEntries() {
        return bookEntryRepository.getBookings().stream().map(b -> new Booking(b.roomID(), b.Monday19())).toList();
    }
    public void addBookEntry(BookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        Booking bookDataEntry = new Booking(UUID.fromString(form.roomID()), form.Monday19());
        
        if (!bookDataEntry.validateBookingCoorectness()) 
            throw new GeneralDomainException();
        bookEntryRepository.addBookEntry(new BookingEntry(UUID.fromString(form.roomID()), form.Monday19()));
    }

    


}
