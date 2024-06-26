package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestClass
public class ExceptionsTest {

    @Test
    @DisplayName("Can throw a GeneralDomainException")
    void test_1() {
        assertThatThrownBy(() -> {
            throw new GeneralDomainException();
        }).isInstanceOf(GeneralDomainException.class);
    }
}

