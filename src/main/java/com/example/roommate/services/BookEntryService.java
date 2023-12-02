package com.example.roommate.services;

import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.interfaces.exceptions.GeneralDomainException;
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

    public List<IBooking> getBookEntries() {
        return bookEntryRepository.getBookings().stream().toList();
    }
    public void addBookEntry(BookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        Booking booking = new Booking(UUID.fromString(form.roomID()), form.Monday19());
        
        if (!booking.validateBookingCoorectness()) 
            throw new GeneralDomainException();
        bookEntryRepository.addBookEntry(booking);
    }

    


}
