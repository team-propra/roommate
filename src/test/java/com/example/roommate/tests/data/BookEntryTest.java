package com.example.roommate.tests.data;

import com.example.roommate.data.BookingEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookEntryTest {
    @Test
    @DisplayName("Can create BookEntry")
    public void test_01() {
        BookingEntry entry = new BookingEntry(null, true);
        assertThat(entry.Monday19()).isTrue();
    }
}
