package com.example.roommate.persistence.postgres;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IUserDAO extends CrudRepository<UserDTO, String> {
    @Query("INSERT INTO users (key_id, handle) VALUES (:keyId, :handle) ON CONFLICT DO NOTHING")
    @Modifying
    void insert(@Param("keyId") UUID keyId, @Param("handle") String handle);

    UserDTO findByHandle(String handle);

    @Query("UPDATE users SET key_id = (:keyId) WHERE handle = (:login)")
    @Modifying
    void registerKey(@Param("keyId") UUID keyId, @Param("login") String login);

    @Query("UPDATE users SET keymaster_name = (:keyMasterName) WHERE key_id = (:key)")
    @Modifying
    void verifyUser(@Param("key") UUID key, @Param("keyMasterName") String keymasterName);

    @Query("INSERT INTO user_role (user_handle, role) VALUES (:handle, :role) ON CONFLICT DO NOTHING")
    @Modifying
    void addRole(@Param("handle") String handle, @Param("role") String role);

    @Query("DELETE FROM user_role WHERE user_handle = :handle AND role = :role")
    @Modifying
    void removeRole(@Param("handle") String handle, @Param("role") String role);

    @Query("SELECT role FROM user_role WHERE user_handle = :handle")
    List<String> findRolesByHandle(@Param("handle") String handle);

    @Query("SELECT user_handle, role FROM user_role")
    List<UserRoleDTO> findAllRoles();
}
