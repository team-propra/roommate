package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record ItemToWorkspaceDTO (@Id UUID id, String itemName, UUID workspaceId){
}
