package com.example.roommate.repositories;

import com.example.roommate.domain.values.BookDataForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookEntryRepository {
    private List<BookDataForm> bookDataFormList = new ArrayList<>();

    public List<BookDataForm> getBookDataFormList() {
        return bookDataFormList;
    }

    public void addBookEntry(BookDataForm bookDataForm) {
        bookDataFormList.add(bookDataForm);
    }
}
