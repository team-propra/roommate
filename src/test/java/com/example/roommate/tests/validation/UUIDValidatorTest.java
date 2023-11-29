package com.example.roommate.tests.validation;

import com.example.roommate.validator.UUIDValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UUIDValidatorTest {
    @Test
    @DisplayName("Call validator with correct uuid")
    public void test_1() {
        String correctUUID = "123e4567-e89b-12d3-a456-426614174000";
        
        UUIDValidator validator = new UUIDValidator();

        assertThat(validator.isValid(correctUUID, null)).isTrue();
    }


    @Test
    @DisplayName("Call validator with incorrect uuid")
    public void test_2() {
        String incorrectUUID = "incorrect uuid";
        
        UUIDValidator validator = new UUIDValidator();

        assertThat(validator.isValid(incorrectUUID, null)).isFalse();
    }

    @Test
    @DisplayName("Call validator with incorrect null")
    public void test_3() {
        String incorrectUUID = null;

        UUIDValidator validator = new UUIDValidator();

        assertThat(validator.isValid(incorrectUUID, null)).isFalse();
    }
}
