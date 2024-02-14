package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user")
public record UserDTO(@Id UUID keyId, String gitHubHandle, String role){
}
