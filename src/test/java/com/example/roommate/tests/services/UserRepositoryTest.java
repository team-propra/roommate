package com.example.roommate.tests.services;

import com.example.repositorytests.repositories.RepositoryBackendsTest;
import com.example.repositorytests.repositories.RepositoryFixture;
import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestClass
public class UserRepositoryTest {
    private static final UUID KEY_ID = UUID.fromString("3e8a586e-b224-410e-9f2d-784ed3517d2c");

    @RepositoryBackendsTest
    @DisplayName("addUser stores all roles and stays idempotent for an existing handle")
    void addUserStoresRolesIdempotently(RepositoryFixture fixture) {
        IUserRepository users = fixture.users();

        users.addUser(new User(KEY_ID, "casey", Set.of("USER", "ADMIN")));
        users.addUser(new User(KEY_ID, "casey", Set.of("ADMIN", "VERIFIED_USER")));

        IUser storedUser = users.getUserByLogin("casey");

        assertThat(storedUser.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN", "VERIFIED_USER");
        assertThat(users.getAllUser())
                .extracting(IUser::getHandle)
                .containsExactly("casey");
    }

    @RepositoryBackendsTest
    @DisplayName("registerKey updates an existing user without losing roles")
    void registerKeyUpdatesExistingUser(RepositoryFixture fixture) {
        IUserRepository users = fixture.users();

        users.addUser(new User(null, "sam", Set.of("USER", "ADMIN")));
        users.registerKey(KEY_ID, "sam");

        IUser storedUser = users.getUserByLogin("sam");

        assertThat(storedUser.getKeyId()).isEqualTo(KEY_ID);
        assertThat(storedUser.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @RepositoryBackendsTest
    @DisplayName("verifyUser adds VERIFIED_USER and stores the keymaster name")
    void verifyUserAddsVerifiedRoleAndKeymasterName(RepositoryFixture fixture) {
        IUserRepository users = fixture.users();

        users.addUser(new User(KEY_ID, "taylor", "USER"));
        users.verifyUser(KEY_ID, "Taylor Keymaster");

        IUser storedUser = users.getUserByLogin("taylor");

        assertThat(storedUser.getRoles()).containsExactlyInAnyOrder("USER", "VERIFIED_USER");
        assertThat(storedUser.getKeyMasterName()).isEqualTo("Taylor Keymaster");
    }

    @RepositoryBackendsTest
    @DisplayName("removeRole removes only the selected role")
    void removeRoleKeepsRemainingRoles(RepositoryFixture fixture) {
        IUserRepository users = fixture.users();

        users.addUser(new User(KEY_ID, "morgan", Set.of("USER", "ADMIN", "VERIFIED_USER")));
        users.removeRole("morgan", "ADMIN");

        assertThat(users.getUserByLogin("morgan").getRoles())
                .containsExactlyInAnyOrder("USER", "VERIFIED_USER");
    }
}
