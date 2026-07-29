package com.example.roommate.tests.services;

import com.example.repositorytests.repositories.RepositoryBackendsTest;
import com.example.repositorytests.repositories.RepositoryFixture;
import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.values.models.UserModel;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestClass
public class AdminApplicationServiceTest {
    private static final UUID KEY_ID = UUID.fromString("42dd1455-ec2d-48bc-a424-baf32c896877");

    @RepositoryBackendsTest
    @DisplayName("getUsers maps stored users to sorted role display models")
    void getUsersReturnsSortedRoleDisplay(RepositoryFixture fixture) {
        AdminApplicationService adminApplicationService =
                new AdminApplicationService(fixture.roomDomainService(), fixture.userDomainService());
        fixture.users().addUser(new User(KEY_ID, "riley", Set.of("VERIFIED_USER", "ADMIN", "USER")));

        UserModel user = adminApplicationService.getUsers().users().getFirst();

        assertThat(user.userName()).isEqualTo("riley");
        assertThat(user.roles()).containsExactlyInAnyOrder("USER", "VERIFIED_USER", "ADMIN");
        assertThat(user.joinedRoles()).isEqualTo("ADMIN, USER, VERIFIED_USER");
    }

    @RepositoryBackendsTest
    @DisplayName("grantAdmin and revokeAdmin update the ADMIN role")
    void grantAndRevokeAdminUpdateRoles(RepositoryFixture fixture) {
        AdminApplicationService adminApplicationService =
                new AdminApplicationService(fixture.roomDomainService(), fixture.userDomainService());
        fixture.users().addUser(new User(KEY_ID, "jamie", "USER"));

        adminApplicationService.grantAdmin("jamie");
        assertThat(adminApplicationService.getUserByHandle("jamie").roles())
                .containsExactlyInAnyOrder("USER", "ADMIN");

        adminApplicationService.revokeAdmin("jamie");
        assertThat(adminApplicationService.getUserByHandle("jamie").roles())
                .containsExactly("USER");
    }
}
