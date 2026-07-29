package com.example.roommate.tests.services;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.application.services.KeyMasterApplicationService;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.persistence.ephemeral.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestClass
public class AuthenticationApplicationServiceTest {
    @Test
    @DisplayName("tryEnsureUserKey creates a user first, then registers a key on the next call")
    void tryEnsureUserKeyCreatesAndRegistersUserKey() {
        UserRepository users = new UserRepository();
        UserDomainService userDomainService = new UserDomainService(users);
        KeyMasterApplicationService keymaster = mock(KeyMasterApplicationService.class);
        UUID keyId = UUID.fromString("23aa261a-ee58-4b1f-b6c6-0d2117a66907");
        when(keymaster.createKey("avery")).thenReturn(keyId);
        AuthenticationApplicationService authenticationApplicationService =
                new AuthenticationApplicationService(userDomainService, keymaster);

        assertThat(authenticationApplicationService.tryEnsureUserKey("avery")).isFalse();
        assertThat(userDomainService.getUserByHandle("avery").getRoles()).containsExactly("USER");

        assertThat(authenticationApplicationService.tryEnsureUserKey("avery")).isTrue();

        IUser storedUser = userDomainService.getUserByHandle("avery");
        assertThat(storedUser.getKeyId()).isEqualTo(keyId);
        assertThat(storedUser.getRoles()).containsExactly("USER");
        assertThat(users.getAllUser()).hasSize(1);
        verify(keymaster).createKey("avery");
    }
}
