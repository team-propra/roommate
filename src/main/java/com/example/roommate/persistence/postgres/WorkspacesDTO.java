package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("workspace")
public record WorkspacesDTO (@Id UUID id, int workspaceNumber, UUID roomId){
}
