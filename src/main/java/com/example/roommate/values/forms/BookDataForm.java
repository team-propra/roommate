package com.example.roommate.values.forms;


import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.BookingDays;

import java.util.UUID;


public record BookDataForm(UUID id, int stepSize){

   



    public static IntermediateBookDataForm addBookingsToForm(Iterable<String> checkedDays, BookDataForm bookDataForm) throws ArgumentValidationException {

        BookingDays bookingDays = new BookingDays(bookDataForm.stepSize);
        bookingDays.initialize(checkedDays);
        return new IntermediateBookDataForm(bookDataForm,bookingDays);
    }
    


}
