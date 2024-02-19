package com.example.roommate.persistence.ephemeral;

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
    List<IUser> users;

    public UserRepository(List<IUser> users) {
        this.users = users.stream().toList();
    }

    public UserRepository() {
        this(new ArrayList<>());
    }

    @Override
    public void addUser(IUser user) {
        users.add(user);
    }

    @Override
    public void registerKey(UUID keyId, String login) {
        IUser user = new UserEntry(keyId, login, "USER");
        users.add(user);
    }

    @Override
    public IUser getUserByLogin(String login) {
        for (IUser u : users) {
            if (u.getHandle().equals(login)) {
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
                IUser updatedUser = new UserEntry(u.getKeyId(),u.getHandle(), "VERIFIED_USER",keymasterName);
                users.set(i, updatedUser);
            }
        }
    }

    @Override
    public List<? extends IUser> getAllUser() {
        List<IUser> result = new ArrayList<>();
        for(IUser user : users) {
            result.add(new UserEntry(user.getKeyId(), user.getHandle(), user.getRole(), user.getKeyMasterName()));
        }
        return result;
    }
}
