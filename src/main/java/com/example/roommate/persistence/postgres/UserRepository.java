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
    UserDAO userDAO;

    public UserRepository(UserDAO userDAO) s{
        this.userDAO = userDAO;
    }

    @Override
    public void addUser(IUser user) {
        userDAO.insert(user.getKeyId(), user.getGitHubHandle(), user.getRole());
    }

    //ToDo Do we need these?
    @Override
    public UUID getKeyId() {

    }

    @Override
    public String getGitHubHandle() {
        return null;
    }

    @Override
    public String getRole() {
        return null;
    }

}
