package com.example.roommate.values.forms;


import com.example.roommate.values.domain.BookingDays;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.validator.IsValidUUID;


public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/int stepSize, BookingDays bookingDays){

    public BookDataForm(String roomID, int stepSize, BookingDays bookingDays){
        //if(roomID == null) throw new IllegalArgumentException(); Collision with test "POST /book redirects to /room/{id} page when BookDataForm is not validated (f.ex.ID is blank)"
        this.roomID = roomID;
        this.stepSize = stepSize;
        this.bookingDays = RoomDomainService.createBookingDays(stepSize);
       // this.bookingDays = new BookingDays(newBookingDays);
        //this.bookingDays = new BookingDays(stepSize);
    }

  /*  public BookDataForm(String roomID, int stepSize){
        this(roomID, stepSize, null);

    }*/


}
