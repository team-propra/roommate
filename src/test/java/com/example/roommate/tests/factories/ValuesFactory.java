package com.example.roommate.tests.factories;

import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.domain.values.Item;
import com.example.roommate.dtos.forms.LoginForm;

import java.util.UUID;

public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createBookDataForm() {
        return new BookDataForm(id.toString(), true);
    }
    public static BookDataForm createInvalidBookDataForm() {
        return new BookDataForm(id.toString(), false);
    }

    public static Item createItem() {
        return new Item("Item");
    }

    public static LoginForm createLoginData() {
        return new LoginForm("user", "password");
    }
}
