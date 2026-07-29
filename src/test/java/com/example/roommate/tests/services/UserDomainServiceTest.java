package com.example.roommate.tests.services;

import com.example.repositorytests.repositories.RepositoryBackendsTest;
import com.example.repositorytests.repositories.RepositoryFixture;
import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.interfaces.entities.IUser;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestClass
public class UserDomainServiceTest {
    private static final UUID KEY_ID = UUID.fromString("1ed62071-e2ee-41bb-a1c9-d54f567e7eb5");

    @RepositoryBackendsTest
    @DisplayName("addAdmin creates a non-revocable injected admin")
    void addAdminCreatesInjectedAdmin(RepositoryFixture fixture) {
        UserDomainService userDomainService = fixture.userDomainService();

        userDomainService.addAdmin("root-admin");

        IUser admin = userDomainService.getUserByHandle("root-admin");
        assertThat(admin.getRoles()).containsExactlyInAnyOrder("ADMIN", "INJECTED_ADMIN");
    }

    @RepositoryBackendsTest
    @DisplayName("addAdmin adds injected admin roles to an existing user")
    void addAdminUpdatesExistingUser(RepositoryFixture fixture) {
        UserDomainService userDomainService = fixture.userDomainService();
        fixture.users().addUser(new User(KEY_ID, "existing-user", "USER"));

        userDomainService.addAdmin("existing-user");

        assertThat(userDomainService.getUserByHandle("existing-user").getRoles())
                .containsExactlyInAnyOrder("USER", "ADMIN", "INJECTED_ADMIN");
    }

    @RepositoryBackendsTest
    @DisplayName("verifyUser delegates verification to the user repository")
    void verifyUserAddsVerifiedRole(RepositoryFixture fixture) {
        UserDomainService userDomainService = fixture.userDomainService();
        fixture.users().addUser(new User(KEY_ID, "verified-user", "USER"));

        userDomainService.verifyUser(KEY_ID, "Verified User");

        IUser user = userDomainService.getUserByHandle("verified-user");
        assertThat(user.getRoles()).containsExactlyInAnyOrder("USER", "VERIFIED_USER");
        assertThat(user.getKeyMasterName()).isEqualTo("Verified User");
    }

    @RepositoryBackendsTest
    @DisplayName("removeRole does not revoke roles from injected admins")
    void injectedAdminRolesCannotBeRevoked(RepositoryFixture fixture) {
        UserDomainService userDomainService = fixture.userDomainService();

        userDomainService.addAdmin("root-admin");
        userDomainService.removeRole("root-admin", "ADMIN");

        assertThat(userDomainService.getUserByHandle("root-admin").getRoles())
                .containsExactlyInAnyOrder("ADMIN", "INJECTED_ADMIN");
    }

    @RepositoryBackendsTest
    @DisplayName("removeRole revokes roles from normal users")
    void normalUserRolesCanBeRevoked(RepositoryFixture fixture) {
        UserDomainService userDomainService = fixture.userDomainService();
        fixture.users().addUser(new User(KEY_ID, "temporary-admin", Set.of("USER", "ADMIN")));

        userDomainService.removeRole("temporary-admin", "ADMIN");

        assertThat(userDomainService.getUserByHandle("temporary-admin").getRoles())
                .containsExactly("USER");
    }
}
