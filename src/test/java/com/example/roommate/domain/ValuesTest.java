package com.example.roommate.domain;

import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.domain.values.Item;
import com.example.roommate.domain.values.LoginData;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ValuesTest {

    @DisplayName("can create BookDataForm")
    @Test
    void test_1() {
        BookDataForm bookDataForm = ValuesFactory.createBookDataForm();
        assertThat(bookDataForm).isInstanceOf(BookDataForm.class);
    }

    @DisplayName("cant create a BookDataForm with Null as UUID")
    @Test
    @Disabled
    void testCreateBookDataFormWithNullUUID() {
        assertThatThrownBy(() -> new BookDataForm(null, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("can create Item")
    @Test
    void test_3() {
        Item item = ValuesFactory.createItem();
        assertThat(item).isInstanceOf(Item.class);
    }

    @DisplayName("can create LoginData")
    @Test
    void test_4() {
        LoginData loginData = ValuesFactory.createLoginData();
        assertThat(loginData).isInstanceOf(LoginData.class);
    }


}
