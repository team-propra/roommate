package com.example.roommate.values.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingDays {
   public int stepsize;
   public List<Boolean> mondayBookings;
   public List<Boolean> tuesdayBookings;
   public List<Boolean> wednesdayBookings;
   public List<Boolean> thursdayBookings;
   public List<Boolean> fridayBookings;
   public List<Boolean> saturdayBookings;
   public List<Boolean> sundayBookings;

      public BookingDays(int stepsize){
         this.stepsize = stepsize;
         int listsize = (24 * 60) / stepsize;
         this.mondayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.tuesdayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.wednesdayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.thursdayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.fridayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.saturdayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
         this.sundayBookings = new ArrayList<>(Collections.nCopies(listsize, false));
      }
   public BookingDays(){
      this.mondayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.tuesdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.wednesdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.thursdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.fridayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.saturdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.sundayBookings = new ArrayList<>(Collections.nCopies(24, true));
   }
}
