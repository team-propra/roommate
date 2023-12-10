package com.example.roommate.services;

import com.example.roommate.data.BookingEntry;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.tests.domain.exceptions.GeneralDomainException;
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
        return bookEntryRepository.getBookings().stream().map(b -> new Booking(b.roomID(), b.bookingDays())).toList();
    }
    public void addBookEntry(BookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        Booking bookDataEntry = new Booking(UUID.fromString(form.roomID()), form.bookingDays());
        
      /*  if (!bookDataEntry.validateBookingCoorectness())
            throw new GeneralDomainException();*/
        bookEntryRepository.addBookEntry(new BookingEntry(UUID.fromString(form.roomID()), form.bookingDays()));


    }

    


}
