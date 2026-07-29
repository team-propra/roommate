package com.example.roommate.values.models;

import java.util.Set;

public record UserModel(String userName, Set<String> roles, String joinedRoles) {
    public UserModel(String userName, Set<String> roles) {
        this(userName, Set.copyOf(roles), String.join(", ", roles));
    }
}
