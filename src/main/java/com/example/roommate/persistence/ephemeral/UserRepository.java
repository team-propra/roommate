package com.example.roommate.persistence.ephemeral;

import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Profile("test")
public class UserRepository implements IUserRepository {
    List<IUser> users = new ArrayList<>();

    public UserRepository(List<IUser> users) {
        this.users = users;
    }

    @Override
    public void addUser(IUser user) {
        users.add(user);
    }

    @Override
    public void registerKey(UUID keyId, String login) {
        User user = new User(keyId, login, "USER");
        users.add(user);
    }

    @Override
    public IUser getUserByLogin(String login) {
        for (int i = 0; i < users.size(); i++) {
            IUser u = users.get(i);
            if(u.getHandle().equals(login)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void verifyUser(UUID key, String keymasterName) {
        for (int i = 0; i < users.size(); i++) {
            IUser u = users.get(i);
            if(u.getKeyId().equals(key)) {
                User updatedUser = new User(u.getKeyId(),u.getHandle(),"VERIFIED_USER", keymasterName);
                users.set(i, updatedUser);
            }
        }
    }

    @Override
    public List<User> getAllUser() {
        List<User> result = new ArrayList<>();
        for(IUser u : users) {
            result.add(new User(u.getKeyId(), u.getHandle(), u.getRole()));
        }
        return result;
    }
}
