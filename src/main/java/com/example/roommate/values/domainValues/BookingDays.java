package com.example.roommate.values.domainValues;

import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.utility.IterableSupport;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record BookingDays(
        int stepSize,
        Iterable<Boolean> mondayBookings,
        Iterable<Boolean> tuesdayBookings,
        Iterable<Boolean> wednesdayBookings,
        Iterable<Boolean> thursdayBookings,
        Iterable<Boolean> fridayBookings,
        Iterable<Boolean> saturdayBookings,
        Iterable<Boolean> sundayBookings
) {
   
   private static int getListSize(int stepSize){
      return (24 * 60) / stepSize;
   }
   private static ArrayList<Boolean> CreateListOfSize(int stepSize)
   {
      return new ArrayList<>(Collections.nCopies(getListSize(stepSize), false));
   }
   BookingDays(int stepSize){
      this(
           stepSize,
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize),
           CreateListOfSize(stepSize)
      );
   }
   
   private BookingDays Initialize(Iterable<String> from) throws ArgumentValidationException {
      
      List<Boolean> mondayBookings = CreateListOfSize(stepSize);
      List<Boolean> tuesdayBookings = CreateListOfSize(stepSize);
      List<Boolean> wednesdayBookings = CreateListOfSize(stepSize);
      List<Boolean> thursdayBookings = CreateListOfSize(stepSize);
      List<Boolean> fridayBookings = CreateListOfSize(stepSize);
      List<Boolean> saturdayBookings = CreateListOfSize(stepSize);
      List<Boolean> sundayBookings = CreateListOfSize(stepSize);
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
               default:
                  throw new ArgumentValidationException(String.format("Unknown day: %d", day));

            }
         }
      }   
      return new BookingDays(
         stepSize,
         mondayBookings,
         tuesdayBookings,
         wednesdayBookings,
         thursdayBookings,
         fridayBookings,
         saturdayBookings,
         sundayBookings
      );
   }


   public static BookingDays from(int stepSize, Iterable<String> from) throws ArgumentValidationException {
      BookingDays bookingDays = new BookingDays(stepSize);
      return bookingDays.Initialize(from);
   }


   


   public Iterable<BookedTimeframe> toBookedTimeframes(){
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

   private void addBookedTimeFrames(List<BookedTimeframe> output, DayOfWeek dayOfWeek, Iterable<Boolean> slots){
      List<Boolean> slotsList = IterableSupport.toList(slots);
      int continuousSlots = 0;
      for (int i = 0; i < slotsList.size(); i++) {
         if(slotsList.get(i)) {
            continuousSlots++;
            continue;
         }
         if(continuousSlots != 0) // found some continuous slots
         {
            //start time
            int firstContinuousSlotIndex = i - continuousSlots;
            int passedMinutesAtStart = stepSize *firstContinuousSlotIndex;
            int passedSecondsAtStart = passedMinutesAtStart * 60;

            //end time
            int passedMinutesAtEnd = stepSize *i;
            int passedSecondsAtEnd = passedMinutesAtEnd * 60;

            //duration
            int durationInSeconds = passedSecondsAtEnd-passedSecondsAtStart;


            output.add(new BookedTimeframe(dayOfWeek,LocalTime.ofSecondOfDay(passedSecondsAtStart), Duration.ofSeconds(durationInSeconds)));
            continuousSlots = 0;
         }
      }
   }
}
