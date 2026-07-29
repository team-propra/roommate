package com.example.roommate.tests.services;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomEntry;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.persistence.ephemeral.UserRepository;
import com.example.roommate.persistence.ephemeral.WorkspaceEntry;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.repositorytests.repositories.RepositoryBackendsTest;
import com.example.repositorytests.repositories.RepositoryFixture;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.domainValues.RoomNumber;
import com.example.roommate.values.forms.KeyMasterForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestClass
public class BookingApplicationServiceTest {

    @DisplayName("adding an invalid bookDataForm results in GeneralDomainException")
    @Test
    void test_2() throws ArgumentValidationException {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();
        IntermediateBookDataForm invalidBookDataForm = ValuesFactory.createInvalidIntermediateBookDataForm();

        assertThatThrownBy(() -> bookingApplicationService.addBookEntry(invalidBookDataForm, "user")).isInstanceOf(GeneralDomainException.class);

    }


    @DisplayName("adding a null BookEntry results in IllegalArgumentException")
    @Test
    void test_3() {
        BookingApplicationService bookingApplicationService = ServiceFactory.createBookingService();

        assertThatThrownBy(() -> {
            bookingApplicationService.addBookEntry(null, "user");
//            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @DisplayName("addRoom() yields getRooms() returning a collection of 1 IRoom")
    @RepositoryBackendsTest
    void test_4(RepositoryFixture fixture) throws NotFoundException {
        BookingApplicationService bookingApplicationService = fixture.bookingApplicationService();
        Room room = EntityFactory.createRoom();

        bookingApplicationService.addRoom(room);
        Collection<IRoom> rooms = bookingApplicationService.getRooms();

        assertThat(rooms).contains(room);
    }

    @DisplayName("getAssociatedBookEntries only exports bookings for verified users")
    @Test
    void associatedBookEntriesRequireVerifiedUserRole() {
        UUID roomId = UUID.fromString("f6f46acd-08e0-43d7-ae62-01a3b3366aad");
        UUID workspaceId = UUID.fromString("ee8b59d1-d178-4f72-ab8b-c1dc4db5302e");
        UUID verifiedKey = UUID.fromString("68b821f4-5fe7-4acd-b894-73386d966dd5");
        RoomRepository rooms = new RoomRepository();
        UserRepository users = new UserRepository();
        BookingApplicationService bookingApplicationService =
                ServiceFactory.createBookingService(rooms, new ItemRepository(), users);

        rooms.add(new RoomEntry(
                roomId,
                new RoomNumber("201"),
                List.of(new WorkspaceEntry(
                        workspaceId,
                        1,
                        List.of(),
                        List.of(
                                new BookedTimeframe(java.time.DayOfWeek.MONDAY, LocalTime.of(8, 0), Duration.ofHours(1), "verified-user"),
                                new BookedTimeframe(java.time.DayOfWeek.MONDAY, LocalTime.of(9, 0), Duration.ofHours(1), "admin-only")
                        )
                ))
        ));
        users.addUser(new User(verifiedKey, "verified-user", Set.of("USER", "VERIFIED_USER")));
        users.addUser(new User(null, "admin-only", Set.of("USER", "ADMIN")));

        assertThat(bookingApplicationService.getAssociatedBookEntries())
                .containsExactly(new KeyMasterForm(workspaceId, verifiedKey));
    }
}
