package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@DomainService
public class UserDomainService {
    IUserRepository userRepository;

    @Autowired
    public UserDomainService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public IUser getUserByLogin(String login) {
        //return new User(null, "Timm", "USER");
       return userRepository.getUserByLogin(login);
    }

    public void addUser(String login) {
        userRepository.addUser(new User(null, login,"USER"));
    }

    public void verifyUser(UUID keyId, String login) {
        userRepository.verifyUser(keyId, login);
    }
}
