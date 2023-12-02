package com.example.roommate.persistence;

import com.example.roommate.interfaces.entities.IBooking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository {
    private final List<IBooking> bookDataFormList = new ArrayList<>();

    public List<IBooking> getBookings() {
        return bookDataFormList;
    }

    public void addBookEntry(IBooking bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
