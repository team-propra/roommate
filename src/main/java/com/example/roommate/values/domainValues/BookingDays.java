package com.example.roommate.values.domainValues;

import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.utility.IterableSupport;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
   BookingDays(int listSize, int stepSize){
      this(
         stepSize,
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false)),
         new ArrayList<>(Collections.nCopies(listSize, false))
      );
   }


   public static boolean validateBookingCoorectness(BookingDays bookingDays) {
      //At least one box is set to true
      Stream<List<Boolean>> bookingStream = Stream.of(IterableSupport.toList(bookingDays.mondayBookings), IterableSupport.toList(bookingDays.tuesdayBookings), IterableSupport.toList(bookingDays.wednesdayBookings), IterableSupport.toList(bookingDays.thursdayBookings), IterableSupport.toList(bookingDays.fridayBookings), IterableSupport.toList(bookingDays.saturdayBookings), IterableSupport.toList(bookingDays.sundayBookings));
      Stream<Boolean> combinedStream = bookingStream.flatMap(List::stream);
      // stream.forEach(System.out::println);
      return combinedStream.anyMatch(value -> value.equals(true));
   }


   private BookingDays Initialize(Iterable<String> from, int listSize) throws ArgumentValidationException {
      List<Boolean> mondayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> tuesdayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> wednesdayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> thursdayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> fridayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> saturdayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
      List<Boolean> sundayBookings = new ArrayList<>((Collections.nCopies(listSize, false)));
    /*  for (String checkedDay2 : from
           ) {
         System.out.println("output  checkeddays: "+checkedDay2);
      }*/

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
      BookingDays bookingDays = new BookingDays((24 * 60) / stepSize,stepSize);
     // System.out.println("bookingdays output: " + bookingDays.Initialize(from, (24 * 60) / stepSize));
      return bookingDays.Initialize(from, (24 * 60) / stepSize);
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
