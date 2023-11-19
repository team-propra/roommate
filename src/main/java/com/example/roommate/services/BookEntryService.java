package com.example.roommate.services;

import com.example.roommate.domain.entities.BookDataEntry;
import com.example.roommate.repositories.BookEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEntryService {
    BookEntryRepository bookEntryRepository;

    public BookEntryService(BookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<BookDataEntry> getBookEntries() {
        return bookEntryRepository.getBookDataFormList();
    }
    public void addBookEntry(BookDataEntry bookDataForm) {
        bookEntryRepository.addBookEntry(bookDataForm);
    }


}
