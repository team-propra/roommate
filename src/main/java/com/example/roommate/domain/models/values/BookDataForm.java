package com.example.roommate.domain.models.values;


import com.example.roommate.validator.IsValidUUID;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import jakarta.validation.constraints.AssertTrue;


import java.util.UUID;

public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/ boolean Monday19 ){}
