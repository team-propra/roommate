package com.example.roommate.frontend;

import com.example.roommate.frontend.utility.TestModel;
import com.example.roommate.frontend.utility.ThymeleafTestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;


import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomDetailsTest {

    @Test
    @DisplayName("Check, that home.html contains the string 'Homepage'")
    @Description("this is a bad test for illustration purposes, as we dont use thymeleaf yet")
    public void test() {
        //Arrange
        ThymeleafTestEngine thymeleafTestEngine = new ThymeleafTestEngine();

        //Act
        TestModel model = new TestModel();
        String uuid = "3c857752-79ed-4fde-a916-770ae34e70e1";
        model.addAttribute("roomID", uuid);
        String render = thymeleafTestEngine.render("roomDetails.html");

        //Assert
        assertThat(render).contains(uuid);
    }

}
