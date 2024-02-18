package com.example.roommate.values.forms;


import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.BookingDays;

import java.util.UUID;


public record BookDataForm(UUID workspaceId, UUID roomId, int stepSize){

   



    public static IntermediateBookDataForm addBookingsToForm(Iterable<String> checkedDays, BookDataForm bookDataForm) throws ArgumentValidationException {

        BookingDays bookingDays = BookingDays.from(bookDataForm.stepSize(),checkedDays);
        return new IntermediateBookDataForm(bookDataForm,bookingDays);
    }


}
