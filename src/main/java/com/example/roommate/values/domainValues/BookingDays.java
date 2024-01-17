package com.example.roommate.values.domainValues;

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


   public String convertToString(){

         List<List<Boolean>> allBookingDays = new ArrayList<>();
         allBookingDays.add(mondayBookings);
         allBookingDays.add(tuesdayBookings);
         allBookingDays.add(wednesdayBookings);
         allBookingDays.add(thursdayBookings);
         allBookingDays.add(fridayBookings);
         allBookingDays.add(saturdayBookings);
         allBookingDays.add(sundayBookings);


      String outpout = "";
      int count = 0;
      int j = 0;
      for(List<Boolean> bookingList : allBookingDays){
         boolean bookingHappened = bookingList.stream().anyMatch(a -> a == true);
         if(bookingHappened == false){
            j++;
            continue;
         }
         switch(j){
            case 0:
               outpout = outpout + "Montag[";
               break;
            case 1:
               outpout =  outpout + "Dienstag[";
               break;
            case 2:
               outpout = outpout + "Mittwoch[";
               break;
            case 3:
               outpout = outpout + "Donnerstag[";
               break;
            case 4:
               outpout = outpout + "Freitag[";
               break;
            case 5:
               outpout = outpout + "Samstag[";
               break;
            case 6:
               outpout = outpout + "Sonntag[";
               break;
         }

         j++;

         boolean ersterDurchlauf =  true;
         for (int i = 0; i < bookingList.size(); i++) {
            LocalTime customTime = LocalTime.of(0, 0);
            count = 0;

            if (bookingList.get(i) == true){

               if (bookingList.get(i+1) == true){
                  i++;
                  while (bookingList.get(i) == true){
                    count++;
                    i++;
                  }

                  if(ersterDurchlauf){
                     outpout = outpout + String.format("%s - %s", customTime.plusMinutes(stepsize * (i-count-1)), customTime.plusMinutes(stepsize * (i-count-1) + stepsize +(count * stepsize)));

                  }
                  else{
                     outpout = outpout + String.format(", %s - %s", customTime.plusMinutes(stepsize * (i - count - 1)), customTime.plusMinutes(stepsize * (i - count - 1) + stepsize + (count * stepsize)));
                  }
                  ersterDurchlauf = false;
                  continue;
               }
               if(ersterDurchlauf){
                  outpout = outpout + String.format("%s - %s", customTime.plusMinutes(stepsize * i), customTime.plusMinutes(stepsize * i + stepsize +(count * stepsize)));

               }
               else {
                  outpout = outpout + String.format(", %s - %s", customTime.plusMinutes(stepsize * i), customTime.plusMinutes(stepsize * i + stepsize + (count * stepsize)));
               }
               ersterDurchlauf = false;
               continue;
            }

         }
         outpout = outpout + "] \n";
         System.out.println();
      }
      return  outpout;
   }

}
