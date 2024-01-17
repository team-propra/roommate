package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
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
   BookingDays(){
      this.mondayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.tuesdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.wednesdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.thursdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.fridayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.saturdayBookings = new ArrayList<>(Collections.nCopies(24, true));
      this.sundayBookings = new ArrayList<>(Collections.nCopies(24, true));
   }


   public static BookingDays createBookingDays(int stepSize){
      BookingDays bookingDays;
      if(stepSize == 0)
         bookingDays = new BookingDays();
      else
         bookingDays = new BookingDays(stepSize);
      
      return bookingDays;
   }


   


   public List<BookedTimeframe> toBookedTimeframes(){
      List<BookedTimeframe> bookedTimeframes = new ArrayList<>();
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.MONDAY,mondayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.TUESDAY,tuesdayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.WEDNESDAY,wednesdayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.THURSDAY,thursdayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.FRIDAY,fridayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.SATURDAY,saturdayBookings);
      addBookedTimeFrames(bookedTimeframes,DayOfWeek.SUNDAY,sundayBookings);
      return bookedTimeframes;
   }

   private void addBookedTimeFrames(List<BookedTimeframe> output, DayOfWeek dayOfWeek, List<Boolean> slots){
      int continuousSlots = 0;
      for (int i = 0; i < slots.size(); i++) {
         if(slots.get(i)) {
            continuousSlots++;
            continue;
         }
         //start time
         int firstContinuousSlotIndex = i - continuousSlots;
         int passedMinutesAtStart = stepsize*firstContinuousSlotIndex;
         int passedSecondsAtStart = passedMinutesAtStart * 60;
         
         //end time
         int passedMinutesAtEnd = stepsize*i;
         int passedSecondsAtEnd = passedMinutesAtEnd * 60;
         
         //duration
         int durationInSeconds = passedSecondsAtEnd-passedSecondsAtStart;
                 
                 
         output.add(new BookedTimeframe(dayOfWeek,LocalTime.ofSecondOfDay(passedSecondsAtStart), Duration.ofSeconds(durationInSeconds)));
         continuousSlots = 0;
      }
   }
}
