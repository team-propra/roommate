package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.domain.services.UserDomainService;

@ApplicationService
public class UserApplicationService {
    UserDomainService userDomainService;

    public UserApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public User getUserByLogin(String login) {
        return userDomainService.getUserByLogin(login);
    }

    public boolean isVerified(String login) {
        return getUserByLogin(login).getRole().equals("VERIFIED_USER");
    }
}
