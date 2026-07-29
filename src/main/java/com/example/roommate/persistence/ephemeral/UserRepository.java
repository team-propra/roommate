package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Profile("test")
public class UserRepository implements IUserRepository {
    List<IUser> users;

    public UserRepository(List<IUser> users) {
        this.users = new ArrayList<>(users);
    }

    public UserRepository() {
        this(new ArrayList<>());
    }

    @Override
    public void addUser(IUser user) {
        if (getUserByLogin(user.getHandle()) != null) {
            user.getRoles().forEach(role -> addRole(user.getHandle(), role));
            return;
        }
        users.add(user);
    }

    @Override
    public void registerKey(UUID keyId, String login) {
        for (int i = 0; i < users.size(); i++) {
            IUser user = users.get(i);
            if (user.getHandle().equals(login)) {
                users.set(i, new UserEntry(keyId, login, user.getRoles(), user.getKeyMasterName()));
                return;
            }
        }
    }

    @Override
    public IUser getUserByLogin(String login) {
        return users.stream().filter(user -> user.getHandle().equals(login)).findFirst().orElse(null);
    }

    @Override
    public void verifyUser(UUID key, String keymasterName) {
        for (int i = 0; i < users.size(); i++) {
            IUser user = users.get(i);
            if (key.equals(user.getKeyId())) {
                Set<String> roles = new HashSet<>(user.getRoles());
                roles.add("VERIFIED_USER");
                users.set(i, new UserEntry(user.getKeyId(), user.getHandle(), roles, keymasterName));
            }
        }
    }

    @Override
    public void addRole(String login, String role) {
        for (int i = 0; i < users.size(); i++) {
            IUser user = users.get(i);
            if (user.getHandle().equals(login)) {
                Set<String> roles = new HashSet<>(user.getRoles());
                roles.add(role);
                users.set(i, new UserEntry(user.getKeyId(), user.getHandle(), roles, user.getKeyMasterName()));
                return;
            }
        }
    }

    @Override
    public void removeRole(String login, String role) {
        for (int i = 0; i < users.size(); i++) {
            IUser user = users.get(i);
            if (user.getHandle().equals(login)) {
                Set<String> roles = new HashSet<>(user.getRoles());
                roles.remove(role);
                users.set(i, new UserEntry(user.getKeyId(), user.getHandle(), roles, user.getKeyMasterName()));
                return;
            }
        }
    }

    @Override
    public List<? extends IUser> getAllUser() {
        return users.stream()
                .map(user -> new UserEntry(user.getKeyId(), user.getHandle(), user.getRoles(), user.getKeyMasterName()))
                .toList();
    }
}
