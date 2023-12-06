package com.example.roommate.annotations;

import com.example.roommate.validator.UUIDValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestClass {
}
