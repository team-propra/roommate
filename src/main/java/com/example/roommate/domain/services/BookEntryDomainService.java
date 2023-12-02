package com.example.roommate.domain.services;

import com.example.roommate.domain.models.entities.Booking;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.interfaces.exceptions.GeneralDomainException;
import com.example.roommate.interfaces.repositories.IBookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEntryDomainService {
    IBookEntryRepository bookEntryRepository;

    public BookEntryDomainService(IBookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<IBooking> getBookEntries() {
        return bookEntryRepository.getBookings().stream().toList();
    }
    
    
    public void addBocking(IBooking booking) throws GeneralDomainException {
        Booking bookingEntity = new Booking(booking.getRoomID(), booking.getMonday19());

        if (!bookingEntity.validateBookingCoorectness())
            throw new GeneralDomainException();
        bookEntryRepository.addBookEntry(bookingEntity);
    }
    
    
}