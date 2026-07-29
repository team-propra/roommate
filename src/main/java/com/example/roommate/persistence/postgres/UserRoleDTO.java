package com.example.roommate.persistence.postgres;

import org.springframework.data.relational.core.mapping.Column;

public record UserRoleDTO(@Column("user_handle") String userHandle, String role) {
}
