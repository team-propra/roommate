package com.example.roommate.values.models;

import java.util.Set;

public record UserModel(String userName, Set<String> roles, String joinedRoles) {
    public UserModel {
        roles = Set.copyOf(roles);
    }

    public UserModel(String userName, Set<String> roles) {
        this(userName, roles, String.join(", ", roles.stream().sorted().toList()));
    }

    @Override
    public Set<String> roles() {
        return Set.copyOf(roles);
    }
}
