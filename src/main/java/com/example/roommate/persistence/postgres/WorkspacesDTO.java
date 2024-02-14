package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record WorkspacesDTO (@Id UUID id, int workspaceNumber, UUID roomId){
}
