package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
public record UserDTO(UUID keyId, @Id String handle, String role){
}
