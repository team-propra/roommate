package com.example.roommate.persistence.postgres;

import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("!test")
public class UserRepository implements IUserRepository {
    IUserDAO userDAO;

    public UserRepository(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void addUser(IUser user) {
        userDAO.insert(user.getKeyId(), user.getHandle(), user.getRole());
    }

    @Override
    public void registerKey(UUID keyId, String login) {
        userDAO.verifyUser(keyId, login);
    }

    @Override
    public IUser getUserByLogin(String login) {
        UserDTO userDTO = userDAO.findByHandle(login);
        if(userDTO == null) {
            return null;
        }
        return new User(userDTO.keyId(), userDTO.handle(), userDTO.role());
    }


}
