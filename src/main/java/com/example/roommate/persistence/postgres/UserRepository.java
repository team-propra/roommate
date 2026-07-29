package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.repositories.IUserRepository;
import com.example.roommate.utility.IterableSupport;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("!test")
@SuppressFBWarnings(value="EI2", justification="IUserDAO are properly injected")
public class UserRepository implements IUserRepository {
    IUserDAO userDAO;

    public UserRepository(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void addUser(IUser user) {
        userDAO.insert(user.getKeyId(), user.getHandle());
        user.getRoles().forEach(role -> userDAO.addRole(user.getHandle(), role));
    }

    @Override
    public void registerKey(UUID keyId, String login) {
        userDAO.registerKey(keyId, login);
    }

    @Override
    public IUser getUserByLogin(String login) {
        UserDTO userDTO = userDAO.findByHandle(login);
        return userDTO == null ? null : toUser(userDTO);
    }

    @Override
    public void verifyUser(UUID key, String keymasterName) {
        userDAO.verifyUser(key, keymasterName);
        getAllUser().stream()
                .filter(user -> key.equals(user.getKeyId()))
                .findFirst()
                .ifPresent(user -> userDAO.addRole(user.getHandle(), "VERIFIED_USER"));
    }

    @Override
    public void addRole(String login, String role) {
        userDAO.addRole(login, role);
    }

    @Override
    public void removeRole(String login, String role) {
        userDAO.removeRole(login, role);
    }

    @Override
    public List<? extends IUser> getAllUser() {
        Map<String, Set<String>> rolesByHandle = userDAO.findAllRoles().stream()
                .collect(Collectors.groupingBy(
                        UserRoleDTO::userHandle,
                        Collectors.mapping(UserRoleDTO::role, Collectors.toSet())
                ));

        return IterableSupport.toList(userDAO.findAll()).stream()
                .map(user -> toUser(user, rolesByHandle.getOrDefault(user.handle(), Set.of())))
                .toList();
    }

    private UserOOP toUser(UserDTO userDTO) {
        return new UserOOP(userDTO.keyId(), userDTO.handle(),
                Set.copyOf(userDAO.findRolesByHandle(userDTO.handle())), userDTO.keymasterName());
    }

    private UserOOP toUser(UserDTO userDTO, Set<String> roles) {
        return new UserOOP(userDTO.keyId(), userDTO.handle(), Set.copyOf(roles), userDTO.keymasterName());
    }
}
