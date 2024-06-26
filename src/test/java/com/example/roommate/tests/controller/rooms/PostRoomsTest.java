package com.example.roommate.tests.controller.rooms;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuthVerifiedUser;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.examples.Officer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
@WebMvcTest
@TestClass
public class PostRoomsTest {


    @Autowired
    MockMvc mvc;

    //@MockBean
   // private BookingApplicationService bookingApplicationService;

    @Autowired
    BookingApplicationService bookingService;

    @Test
    @DisplayName("Same checkboxes remain checked after booking")
    @WithMockOAuthVerifiedUser
    //@WithCustomMockUser
    void test_1() throws Exception {
      //  List<String> checkedDays = List.of("0-0-X", "0-1");
       // String roomId = ValuesFactory.id.toString();
        Room room = Officer.Room();
        Workspace workspace = Officer.Workspace();
        bookingService.addRoom(room);
       // bookingApplicationService.addRoom(room);
        // when(bookingApplicationService.findRoomByID(room.getRoomID())).thenReturn(room);
        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("roomId", room.getRoomID().toString())
                        .param("workspaceId", workspace.getId().toString())
                        //.param("cell", checkedDays.toString())
                       // .param("cell", test)
                        .param("cell", "0-0", "0-1-X", "0-2", "0-3")
                        .param("stepSize", "60"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    ModelAndView modelAndView = result.getModelAndView();
                    assertThat(modelAndView).isNotNull();
                  //  assertThat(modelAndView.getView()).isInstanceOf(RedirectView.class);
                    assertThat(modelAndView.getViewName()).isEqualTo("redirect:/"); //starts with is bad here
                    //assertThat(((RedirectView) modelAndView.getView()).getUrl()).isEqualTo("redirect:/");
                });

        //verify(bookingApplicationService, times(1)).addBookEntry(any());

        mvc.perform(get("/room/{id}/workspace/{workspaceId}", room.getRoomID(), workspace.getId().toString()))
                .andExpect(xpath("//input[@id='0-0' and @type='checkbox' and not(@checked='checked')]").exists())
                .andExpect(xpath("//input[@id='0-1' and @type='checkbox' and @checked='checked']").exists())
                .andExpect(xpath("//input[@id='0-2' and @type='checkbox' and not(@checked='checked')]").exists())
                .andExpect(xpath("//input[@id='0-3' and @type='checkbox' and not(@checked='checked')]").exists());
        
                // I get what is being checked here, but it's not readable, im sorry
                //.andExpect(xpath("//input[@type='hidden' and @name='cell' and @value='0-1']/following-sibling/following-sibling::input[@type='checkbox' and @name='box' and @class='checked']").exists());


    }
}
