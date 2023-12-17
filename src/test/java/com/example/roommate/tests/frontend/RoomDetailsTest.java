package com.example.roommate.tests.frontend;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.controller.RoomController;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.utility.thymeleaf.TestModel;
import com.example.roommate.utility.thymeleaf.ThymeleafTestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@TestClass
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

        //stepSize needs to be >= 1
        RoomController.DayTimeFrame dummyDayTimeFrame = new RoomController.DayTimeFrame(0,0,1,new ArrayList<>(),new ArrayList<>());
        model.addAttribute("frame",dummyDayTimeFrame);
        String render = thymeleafTestEngine.render("roomDetails.html",model);

        //Assert
        System.out.println(render);
        assertThat(render).contains(room.getRoomNumber());
    }

}
