package com.example.roommate.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class BookDataEntryTest {


    //What do you test in BookDataEntry? I have no idea
    @Test
    @DisplayName("")
    public void test_01(){
        BookDataEntry entry = new BookDataEntry(null, false);
        assertThat(entry.Monday19()).isFalse();
    }
}
