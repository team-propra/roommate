package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.interfaces.entities.IBooking;

import java.util.List;

@RepositoryInterface
public interface IBookEntryRepository {
    void addBookEntry(IBooking bookDataForm);
    List<IBooking> getBookings();
}
