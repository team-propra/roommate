package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestClass
public class ValuesTest {

    @DisplayName("can create BookDataForm")
    @Test
    void test_1() {
        BookDataForm bookDataForm = ValuesFactory.createValidBookDataForm();
        assertThat(bookDataForm).isInstanceOf(BookDataForm.class);
    }

    @DisplayName("cant create a BookDataForm with Null as UUID")
    @Test
    @Disabled
    void testCreateBookDataFormWithNullUUID() {
        assertThatThrownBy(() -> new BookDataForm(null,null, 60))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("can create Item")
    @Test
    void test_3() {
        ItemName item = ValuesFactory.createItemName();
        assertThat(item).isInstanceOf(ItemName.class);
    }

    @DisplayName("DaytimeFrame correctly converts bookings")
    @Test
    void test_4() throws ArgumentValidationException {
        Iterable<String> checkedDays = List.of("0-0-X", "1-0-X", "2-0-X","1-3-X", "2-3-X");
        BookingDays bookingDays = BookingDays.from(60,checkedDays);

        Iterable<BookedTimeframe> bookedTimeframes = bookingDays.toBookedTimeframes("SomeUser");

        DayTimeFrame dayTimeFrame =  DayTimeFrame.from(bookedTimeframes);

        String bookedTimes = dayTimeFrame.convertToString();

        assertThat(bookedTimes).contains("Montag[00:00 - 03:00]");
        assertThat(bookedTimes).contains("Donnerstag[01:00 - 03:00]");

    }
}
