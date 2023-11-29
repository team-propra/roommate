package com.example.roommate.interfaces;

import com.example.roommate.domain.models.entities.BookingEntity;

import java.util.List;

public interface IBookEntryRepository {
    public List<BookingEntity> getBookDataFormList();
    public void addBookEntry(BookingEntity bookDataForm);
}
