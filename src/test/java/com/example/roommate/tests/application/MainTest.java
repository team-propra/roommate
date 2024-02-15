package com.example.roommate.tests.application;

import com.example.roommate.RoomMateApplication;
import com.example.roommate.annotations.TestClass;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@TestClass
public class MainTest {
    @Test
    @Disabled // postgres database required so it always fails
    @DisplayName("Main can be run")
    public void test_1() {
        RoomMateApplication.main(new String[]{});
    }
}
