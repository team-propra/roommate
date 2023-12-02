package com.example.roommate.interfaces.repositories;

import com.example.roommate.interfaces.entities.IBooking;

import java.util.List;

public interface IBookEntryRepository {
    void addBookEntry(IBooking bookDataForm);
    List<IBooking> getBookings();
}
