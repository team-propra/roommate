package com.example.roommate.values.models;

import java.util.List;

public record UsersModel (List<UserModel> users){
    public UsersModel {
        users = List.copyOf(users);
    }

    @Override
    public List<UserModel> users() {
        return List.copyOf(users);
    }
}
