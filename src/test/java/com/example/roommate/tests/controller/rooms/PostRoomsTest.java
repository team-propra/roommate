package com.example.roommate.tests.controller.rooms;

import com.example.roommate.annotations.TestClass;
//import com.example.roommate.annotations.WithCustomMockUser;
import com.example.roommate.annotations.WithMockOAuth2User;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
@WebMvcTest
@TestClass
public class PostRoomsTest {


    @Autowired
    MockMvc mvc;

    @Mock
    private BookingApplicationService bookingApplicationService;

    @Test
    @DisplayName("")
    @WithMockOAuth2User
    //@WithCustomMockUser
    void test_1() throws Exception {
        List<String> checkedDays = List.of("0-0", "0-1");
        String roomId = ValuesFactory.id.toString();

        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", roomId)
                        .param("checkedDays", checkedDays.toString())
                        .param("stepSize", "60"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    ModelAndView modelAndView = result.getModelAndView();
                    assertThat(modelAndView).isNotNull();
                    assertThat(modelAndView.getView()).isInstanceOf(RedirectView.class);
                    assertThat(((RedirectView) modelAndView.getView()).getUrl()).isEqualTo("redirect:/");
                });

        verify(bookingApplicationService, times(1)).addBookEntry(any());
    }
}
