package com.example.roommate.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.interfaces.exceptions.GeneralDomainException;
import com.example.roommate.dtos.forms.BookDataForm;

import java.util.Collection;
import java.util.UUID;

@ApplicationService
public class BookingApplicationService {
    
    BookEntryDomainService bookEntryDomainService;

    public BookingApplicationService(BookEntryDomainService bookEntryDomainService) {
        this.bookEntryDomainService = bookEntryDomainService;
    }
    
    public Collection<IBooking> getBookEntries() {
        return bookEntryDomainService.getBookEntries();
    }

    public void addBookEntry(BookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        bookEntryDomainService.addBocking(new Booking(UUID.fromString(form.roomID()), form.Monday19()));
    }

    


}
