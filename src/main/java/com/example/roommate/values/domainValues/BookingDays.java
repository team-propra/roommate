package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingDays {
   public int stepsize;
   
   List<Boolean> mondayBookings;
   List<Boolean> tuesdayBookings;
   List<Boolean> wednesdayBookings;
   List<Boolean> thursdayBookings;
   List<Boolean> fridayBookings;
   List<Boolean> saturdayBookings;
   List<Boolean> sundayBookings;

   public BookingDays(int stepSize, Iterable<String> checkedDays){


      this.stepsize = stepSize;
      int listSize = (24 * 60) / stepSize;
      this.mondayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.tuesdayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.wednesdayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.thursdayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.fridayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.saturdayBookings = new ArrayList<>(Collections.nCopies(listSize, false));
      this.sundayBookings = new ArrayList<>(Collections.nCopies(listSize, false));

      Initialize(checkedDays);
   }
   
   void Initialize(Iterable<String> from){
      for (String checkedDay : from) {

         if(checkedDay.contains("-X")) {
            String[] daytime = checkedDay.split("-");

            int timeIndex = Integer.parseInt(daytime[0]);
            int day = Integer.parseInt(daytime[1]);//0=monday, 1=tuesday...
            switch(day){
               case 0:
                  mondayBookings.set(timeIndex, true);
                  break;
               case 1:
                  tuesdayBookings.set(timeIndex, true);
                  break;
               case 2:
                  wednesdayBookings.set(timeIndex, true);
                  break;
               case 3:
                  thursdayBookings.set(timeIndex, true);
                  break;
               case 4:
                  fridayBookings.set(timeIndex, true);
                  break;
               case 5:
                  saturdayBookings.set(timeIndex, true);
                  break;
               case 6:
                  sundayBookings.set(timeIndex, true);
                  break;

            }
         }
      }   
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


   public static BookingDays createBookingDays(int stepSize, Iterable<String> checkedDays){
      BookingDays bookingDays;
      if(stepSize == 0)
         bookingDays = new BookingDays();
      else
         bookingDays = new BookingDays(stepSize, checkedDays);
      
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
         if(continuousSlots != 0) // found some continuous slots
         {
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
}
