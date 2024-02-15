package com.example.roommate.annotations;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test") //ensure test classes use ephemeral repositories
public @interface TestClass {
}
