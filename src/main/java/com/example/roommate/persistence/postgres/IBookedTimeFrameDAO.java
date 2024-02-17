package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Interface
public interface IBookedTimeFrameDAO extends CrudRepository<BookedTimeframeDTO, UUID> {
    @Query("INSERT INTO booked_timeframe (id, day_of_week,local_time,duration,room_id) VALUES (:id, :day, :startTime, :duration, :workspaceId)")
    @Modifying
    void insert(@Param("id") UUID id,@Param("day") DayOfWeek day,@Param("startTime") LocalTime startTime,@Param("duration") Duration duration,@Param("workspaceId") UUID workspaceId);
}
