package com.example.roommate.domain;

import com.example.roommate.domain.exceptions.GeneralDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExceptionsTest {

    @Test
    @DisplayName("Can throw a GeneralDomainException")
    void test_1() {
        assertThatThrownBy(() -> {
            throw new GeneralDomainException();
        }).isInstanceOf(GeneralDomainException.class);
    }
}

