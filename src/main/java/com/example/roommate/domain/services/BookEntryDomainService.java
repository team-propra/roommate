package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.values.domain.BookingDays;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.repositories.IBookEntryRepository;

import java.util.List;

@DomainService
public class BookEntryDomainService {
    IBookEntryRepository bookEntryRepository;

    public BookEntryDomainService(IBookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<IBooking> getBookEntries() {
        return bookEntryRepository.getBookings().stream().toList();
    }
    
    
    public void addBocking(IBooking booking) throws GeneralDomainException {
        Booking bookingEntity = new Booking(booking.getRoomID(), new BookingDays());

        if (!bookingEntity.validateBookingCoorectness())
            throw new GeneralDomainException();
        bookEntryRepository.addBookEntry(bookingEntity);
    }
    
    
}
