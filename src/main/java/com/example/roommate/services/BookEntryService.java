package com.example.roommate.services;

import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.BookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEntryService {
    BookEntryRepository bookEntryRepository;

    public BookEntryService(BookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<BookDataForm> getBookEntries() {
        return bookEntryRepository.getBookDataFormList();
    }
    public void addBookEntry(BookDataForm bookDataForm) {
        bookEntryRepository.addBookEntry(bookDataForm);
    }


}
