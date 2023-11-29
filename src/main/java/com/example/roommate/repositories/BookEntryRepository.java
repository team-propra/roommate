package com.example.roommate.repositories;

import com.example.roommate.domain.models.entities.BookingEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository {
    private final List<BookingEntity> bookDataFormList = new ArrayList<>();

    public List<BookingEntity> getBookDataFormList() {
        return bookDataFormList;
    }

    public void addBookEntry(BookingEntity bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
