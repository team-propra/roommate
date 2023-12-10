package com.example.roommate.persistence;

import com.example.roommate.data.BookingEntry;
import com.example.roommate.domain.models.values.CalendarDay;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class BookEntryRepository {

    private final List<BookingEntry> bookDataFormList = new ArrayList<>();

    public BookEntryRepository(){

    }

    public List<BookingEntry> getBookings() {
        return bookDataFormList;
    }

    public void addBookEntry(BookingEntry bookDataForm) {
        bookDataFormList.add(bookDataForm);

    }
}
