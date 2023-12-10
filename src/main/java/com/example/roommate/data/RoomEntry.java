package com.example.roommate.data;

import com.example.roommate.domain.models.values.CalendarDay;

import java.util.UUID;

public record RoomEntry(UUID roomID, String roomnumber,
                        CalendarDay monday,//will be replaced by an actual calendar day, prob. using java.time.LocalDate for this, maybe somth. like: List<LocalDate,CalendarDay>
                                CalendarDay tuesday,
                                CalendarDay wednesday,
                                CalendarDay thursday,
                                CalendarDay friday,
                                CalendarDay saturday,
                                CalendarDay sunday) {

        public RoomEntry(UUID roomID, String roomnumber){
            this(roomID, roomnumber,new CalendarDay(),
                    new CalendarDay(),
                    new CalendarDay(),
                    new CalendarDay(),
                    new CalendarDay(),
                    new CalendarDay(),
                    new CalendarDay());



        }

}