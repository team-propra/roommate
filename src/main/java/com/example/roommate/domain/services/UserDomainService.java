package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.repositories.IUserRepository;

@DomainService
public class UserDomainService {
    IUserRepository userRepository;

    public UserDomainService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByLogin(String login) {
        return new User(null, "Timm", "USER");
    }
}
