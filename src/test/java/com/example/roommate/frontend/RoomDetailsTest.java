package com.example.roommate.frontend;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.frontend.utility.TestModel;
import com.example.roommate.frontend.utility.ThymeleafTestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.support.BindStatus;


import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomDetailsTest {

    @Test
    @DisplayName("RoomDetails should dynamically render a roomnumber from scope")
    @Description("this is a bad test for illustration purposes, as we dont use thymeleaf yet")
    public void test() {
        //Arrange
        ThymeleafTestEngine thymeleafTestEngine = new ThymeleafTestEngine();

        //Act
        TestModel model = new TestModel();
        UUID uuid = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room room = new Room(uuid,"whatever");
        model.addAttribute("room", room);
        String render = thymeleafTestEngine.render("roomDetails.html",model);

        //Assert
        assertThat(render).contains(room.getRoomnumber());
    }

}
