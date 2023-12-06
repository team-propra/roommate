package com.example.roommate.persistence.repositories;

import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.interfaces.repositories.IBookEntryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository implements IBookEntryRepository {
    private final List<IBooking> bookDataFormList = new ArrayList<>();

    public List<IBooking> getBookings() {
        return bookDataFormList;
    }

    public void addBookEntry(IBooking bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
