package com.example.roommate.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Interface
public @interface RepositoryInterface {
}
