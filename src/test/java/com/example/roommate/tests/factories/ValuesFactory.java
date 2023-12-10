package com.example.roommate.tests.factories;

import com.example.roommate.domain.models.entities.BookingDays;
import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.domain.models.values.ItemName;
import com.example.roommate.dtos.forms.LoginForm;

import java.util.UUID;

public class ValuesFactory {


    public static UUID id = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    public static BookDataForm createBookDataForm() {
        return new BookDataForm(id.toString(), 60, new BookingDays());
    }
    public static BookDataForm createInvalidBookDataForm() {//doesnt match new BookDataForm
        return new BookDataForm(id.toString(), 60,new BookingDays());
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
