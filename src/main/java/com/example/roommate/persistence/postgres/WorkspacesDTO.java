package com.example.roommate.persistence.postgres;

import java.util.UUID;

public record WorkspacesDTO (UUID id, int workspaceNumber, UUID roomId){
}
