package com.example.roommate.values.forms;


import com.example.roommate.validator.IsValidUUID;



public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/ boolean Monday19 ){}
