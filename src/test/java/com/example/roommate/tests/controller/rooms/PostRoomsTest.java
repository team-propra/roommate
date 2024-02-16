package com.example.roommate.tests.controller.rooms;

import com.example.roommate.annotations.TestClass;
//import com.example.roommate.annotations.WithCustomMockUser;
import com.example.roommate.annotations.WithMockOAuth2User;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.swing.text.Document;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    @WithMockOAuth2User
    //@WithCustomMockUser
    void test_1() throws Exception {
      //  List<String> checkedDays = List.of("0-0-X", "0-1");
       // String roomId = ValuesFactory.id.toString();
        Room room = EntityFactory.createRoom();
        bookingService.addRoom(room);
       // bookingApplicationService.addRoom(room);
        // when(bookingApplicationService.findRoomByID(room.getRoomID())).thenReturn(room);
        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", room.getRoomID().toString())
                        //.param("cell", checkedDays.toString())
                       // .param("cell", test)
                        .param("cell", "0-0", "0-1-X", "0-2", "0-3")
                        .param("stepSize", "60"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    ModelAndView modelAndView = result.getModelAndView();
                    assertThat(modelAndView).isNotNull();
                  //  assertThat(modelAndView.getView()).isInstanceOf(RedirectView.class);
                    assertThat(modelAndView.getViewName()).startsWith("redirect:/");
                    //assertThat(((RedirectView) modelAndView.getView()).getUrl()).isEqualTo("redirect:/");
                });

        //verify(bookingApplicationService, times(1)).addBookEntry(any());

        mvc.perform(get("/room/{id}", room.getRoomID()))
                //.andExpect(xpath("(//input[contains(@class,'checked')])[2]"));
                .andExpect(xpath("//input[@type='hidden' and @name='cell' and @value='0-1']/following-sibling::input[@type='checkbox' and @name='box' and @class='checked']").exists());


    }
}
