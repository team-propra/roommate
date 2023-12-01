package com.example.roommate.persistence;

import com.example.roommate.persistence.data.BookingEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository {
    private final List<BookingEntry> bookDataFormList = new ArrayList<>();

    public List<BookingEntry> getBookings() {
        return bookDataFormList;
    }

    public void addBookEntry(BookingEntry bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
