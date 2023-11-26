package com.example.roommate.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.print.Book;

import static org.assertj.core.api.Assertions.assertThat;

public class BookEntryTest {
    @Test
    @DisplayName("Can create BookEntry")
    public void test_01() {
        BookDataEntry entry = new BookDataEntry(null, true);
        assertThat(entry.Monday19()).isTrue();
    }
}
