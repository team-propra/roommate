package com.example.roommate.persistence.postgres;

import com.example.roommate.domain.models.entities.User;
import com.example.roommate.interfaces.entities.IUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserDAO extends CrudRepository<UserDTO, UUID> {
    @Query("INSERT INTO users (key_id, handle, role) VALUES (:keyId, :handle, :role)")
    void insert(UUID keyId, String handle, String role);



}
