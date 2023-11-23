package com.example.roommate.factories;

import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.domain.values.Item;
import com.example.roommate.domain.values.LoginData;

import java.util.UUID;

public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createBookDataForm() {
        return new BookDataForm(id, true);
    }
    public static BookDataForm createInvalidBookDataForm() {
        return new BookDataForm(id, false);
    }

    public static Item createItem() {
        return new Item("Item");
    }

    public static LoginData createLoginData() {
        return new LoginData("user", "password");
    }
}
