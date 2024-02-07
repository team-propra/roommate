package com.example.roommate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockOAuth2User(login = "admin", id = 1234, roles = {"ADMIN"})
public @interface WithMockOAuthAdmin {
}
