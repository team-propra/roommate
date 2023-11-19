package com.example.roommate.repositories;

import com.example.roommate.domain.entities.BookDataEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository {
    private final List<BookDataEntry> bookDataFormList = new ArrayList<>();

    public List<BookDataEntry> getBookDataFormList() {
        return bookDataFormList;
    }

    public void addBookEntry(BookDataEntry bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
