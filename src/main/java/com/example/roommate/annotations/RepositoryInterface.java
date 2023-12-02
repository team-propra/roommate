package com.example.roommate.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Documented
@Service
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryInterface {
}
