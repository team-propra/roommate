package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.controller.RoomController.DayTimeFrame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestClass
public class DayTimeFrameTest {


    @DisplayName("DayTimeFrame when number of days does not equal the number of day-names given")
    @Test
    void test_1() {
        assertThatThrownBy(() -> new DayTimeFrame(0, 0, 0, List.of("Monday"),List.of(""))).isInstanceOf(RuntimeException.class);
    }
}
