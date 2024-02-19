package com.example.roommate.tests.controller.booking;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuthVerifiedUser;
import com.example.roommate.application.services.KeyMasterApplicationService;
import com.example.roommate.controller.RoomController;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.examples.Officer;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RoomController.class)
@TestClass
public class PostBookTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingApplicationService entryService;

    @MockBean
    RoomDomainService roomDomainService;

    @MockBean
    RoomRepository roomRepository;

    @MockBean
    KeyMasterApplicationService keyMasterService;

    @MockBean
    UserDomainService userDomainService;

    UUID roomID = ValuesFactory.roomId;
    UUID workspaceId = ValuesFactory.roomId;

    BookDataForm bookDataForm = new BookDataForm(workspaceId, roomID,60);

    @DisplayName("unsuccessful POST /rooms should not redirects to / if workspaceId is missing")
    @Test
    @WithMockOAuthVerifiedUser
    void test_1() throws Exception {
        roomRepository.add(ValuesFactory.createRoomEntry("randomroomnumber"));
        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", roomID.toString())
                        .param("cell", "0-1-X")//otherwise invalid
                       // .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(status().isBadRequest());
    }

    //future integration test
    //second mvc.perform might instantiate new services each time (so other solution necessary)
    @Disabled
    @DisplayName("Booking Details appear on the homepage after submiting the booking form")
    @Test
    void test_2() throws Exception {
        //   BookDataForm bookDataForm = new BookDataForm(id.toString(),true);

        MvcResult postResult = mvc.perform(post("/rooms")
                        .param("id", roomID.toString())
                        // .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andReturn();
        MvcResult getResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(postResult.getResponse().getHeader("location")).contains("/");
        String html = getResult.getResponse().getContentAsString();
        assertThat(html).contains("%s".formatted(roomID));
    }

    @Test
    @DisplayName("POST /rooms redirects to /room/{id} page when checkedDays are invalid")
    @WithMockOAuthVerifiedUser
    public void test_3() throws Exception {

        mvc.perform(post("/rooms")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("roomId", Officer.RoomID().toString())
                .param("workspaceId", Officer.WorkspaceID().toString())
               // .param("Monday19", Boolean.toString(wrongForm.Monday19())))
                .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/room/*/workspace/*")) ;

    }


    //
    @Test
    @DisplayName("POST /rooms returns Bad-Request and 400 status if BookEntryService.addBookEntry " +
            "throws GeneralDomainException")
    @WithMockOAuthVerifiedUser
    public void test_4() throws Exception {
        //  BookDataForm bookDataForm = new BookDataForm(id.toString(),true);
        //entryService = mock(BookEntryService.class);
        //Mockito.doThrow(new GeneralDomainException()).when(entryService).addBookEntry(bookDataForm);
        Mockito.doThrow(new NotFoundException()).when(entryService).addBookEntry(Mockito.any(), Mockito.anyString());

        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                       // .param("form", bookDataForm.toString()))
                        .param("roomId", Officer.RoomID().toString())
                        .param("workspaceId", Officer.WorkspaceID().toString())
                          .param("cell", "0-1-X")//otherwise invalid
                        //   .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(view().name("bad-request"))
                .andExpect(status().is(400));
    }

    @Test
    @Disabled
    @WithMockOAuthVerifiedUser
    @DisplayName("A VerifiedUser has no problems booking rooms, POST-request yields redirection ")
    public void test_05() throws Exception {
        mvc.perform(get("/rooms")
                        .param("id", EntityFactory.id.toString())
                        .param("stepsize", "60"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
