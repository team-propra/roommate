package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Interface
public interface IBookedTimeFrameDAO extends CrudRepository<BookedTimeframeDTO, UUID> {
    List<BookedTimeframeDTO> findByRoomId(UUID roomID);

    @Query("INSERT INTO booked_timeframe (id, day_of_week,local_time,duration,room_id) VALUES (:id, :day, :startTime, :duration, :roomId)")
    @Modifying
    void insert(@Param("id") UUID id,@Param("day") DayOfWeek day,@Param("startTime") LocalTime startTime,@Param("duration") Duration duration,@Param("roomId") UUID roomId);
}
