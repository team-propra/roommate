package com.example.roommate.interfaces.application.services;

import com.example.roommate.interfaces.entities.IUser;

public interface IAuthenticationApplicationService {
    IUser getUserByLogin(String login);
}
