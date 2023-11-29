package com.example.roommate.dtos.forms;


import com.example.roommate.validator.IsValidUUID;


public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/ boolean Monday19 ){}
