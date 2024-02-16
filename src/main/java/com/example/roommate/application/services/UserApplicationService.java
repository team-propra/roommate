package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.domain.services.UserDomainService;

import java.util.UUID;

@ApplicationService
public class UserApplicationService {
    UserDomainService userDomainService;

    public UserApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public User getUserByLogin(String login) {
        return (User) userDomainService.getUserByLogin(login);
    }

    public boolean isVerified(String login) {
        if(getUserByLogin(login) == null) {
            userDomainService.addUser(login);
            return false;
        }
        else {
            return getUserByLogin(login).getRole().equals("VERIFIED_USER");
        }
    }

    public void verifyUser(UUID keyId, String login) {
        userDomainService.verifyUser(keyId, login);
    }

    public boolean userHasKey(String login) {
        if(userDomainService.getUserByLogin(login) == null) {
            userDomainService.addUser(login);
            return false;
        }
        User user = (User) userDomainService.getUserByLogin(login);
        return user.getKeyId() != null;
    }
}
