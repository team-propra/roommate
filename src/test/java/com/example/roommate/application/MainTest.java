package com.example.roommate.application;

import com.example.roommate.RoomMateApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class MainTest {
    @Test
    @DisplayName("Main can be run")
    public void test_1() {
        RoomMateApplication.main(new String[]{});
    }
}
