package com.example.roommate.domain.values;

import jakarta.validation.constraints.AssertTrue;
import jdk.jfr.BooleanFlag;
import org.springframework.lang.NonNull;


import java.util.UUID;

public record BookDataForm(@NonNull  UUID roomID, /*@AssertTrue*/ boolean Monday19 ){}
