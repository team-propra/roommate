package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        assertThatThrownBy(() -> new BookDataForm(null, 60))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("can create Item")
    @Test
    void test_3() {
        ItemName item = ValuesFactory.createItemName();
        assertThat(item).isInstanceOf(ItemName.class);
    }


}
