package com.example.roommate.factories;

import com.example.roommate.annotations.Factory;
import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.values.domain.ItemName;
import com.example.roommate.dtos.forms.LoginForm;

import java.util.UUID;

@Factory
public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createBookDataForm() {
        return new BookDataForm(id.toString(), true);
    }
    public static BookDataForm createInvalidBookDataForm() {
        return new BookDataForm(id.toString(), false);
    }

    public static ItemName createItemName() {
        return new ItemName("Item");
    }

    public static ItemName createItemName(String type) {
        return new ItemName(type);
    }


    public static LoginForm createLoginData() {
        return new LoginForm("user", "password");
    }
}
