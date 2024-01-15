package com.example.roommate.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin", password = "1234", roles = {"ADMIN"})
public @interface WithCustomMockAdmin {
    // You can add additional attributes if needed
}

