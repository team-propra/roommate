package com.example.roommate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockOAuth2User(login = "verified_user", id = 1234, roles = {"VERIFIED_USER"})
public @interface WithMockOAuthVerifiedUser {
}
