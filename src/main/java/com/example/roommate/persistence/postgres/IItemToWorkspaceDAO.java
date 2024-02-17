package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
@Interface
public interface IItemToWorkspaceDAO extends CrudRepository<ItemToWorkspaceDTO, UUID> {

    List<ItemToWorkspaceDTO> findByWorkspaceId(UUID workspaceId);

    @Query("INSERT INTO item_to_workspace (id, item_name, workspace_id) VALUES (:id, :itemName, :workspaceId)")
    @Modifying
    void insert(@Param("id")UUID id, @Param("itemName") String itemName, @Param("workspaceId") UUID workspaceId);

}