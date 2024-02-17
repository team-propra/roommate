package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


@Interface
public interface IWorkspaceDAO extends CrudRepository<WorkspacesDTO, UUID> {

    @Query("INSERT INTO workspace (id,workspace_number, room_id) VALUES (:id, :workspaceNumber, :roomId)")
    @Modifying
    public void insert(@Param("id") UUID id, @Param("workspaceNumber") int workspaceNumber, @Param("roomId") UUID roomId);
}
