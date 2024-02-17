package com.example.roommate.persistence.postgres;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface IUserDAO extends CrudRepository<UserDTO, String> {
    @Query("INSERT INTO users (key_id, handle, role) VALUES (:keyId, :handle, :role)")
    @Modifying
    void insert(@Param("keyId") UUID keyId, @Param("handle")String handle, @Param("role") String role);

    UserDTO findByHandle(String handle);

    @Query("UPDATE users SET key_id = (:keyId)  WHERE handle = (:login)")
    @Modifying
    void registerKey(@Param("keyId") UUID keyId, @Param("login") String login);

    @Query("UPDATE users SET keymaster_name = (:keyMasterName), role = 'VERIFIED_USER' WHERE key_id = (:key)")
    @Modifying
    void verifyUser(@Param("key")UUID key, @Param("keyMasterName") String keymasterName);
}
