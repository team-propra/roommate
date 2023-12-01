package com.example.roommate.tests.persistence.data;

import com.example.roommate.persistence.data.BookDataEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
public class BookDataEntryTest {

    @DisplayName("Can create a bookDataEntry")
    @Test
    void test_1() {
        BookDataEntry entry = new BookDataEntry(null, true);
        assertThat(entry).isInstanceOf(BookDataEntry.class);

    }
}
