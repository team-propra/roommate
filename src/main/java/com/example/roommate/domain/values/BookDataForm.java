package com.example.roommate.domain.values;


import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import jakarta.validation.constraints.AssertTrue;


import java.util.UUID;

public record BookDataForm(@NotBlank @NonNull  UUID roomID, /*@AssertTrue*/ boolean Monday19 ){}
