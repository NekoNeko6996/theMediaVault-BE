package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository;

import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.FileEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * 
 */
@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, String> {

    @Query("SELECT f FROM FileEntity f WHERE f.name = :name " +
        "AND ((:parentId IS NULL AND f.parent.id IS NULL) OR (f.parent.id = :parentId)) " +
        "AND f.owner.id = :ownerId")
    Optional<FileEntity> findByNameAndParentAndOwner(String name, String parentId, String ownerId);
    
    @Query("SELECT COUNT(f) > 0 FROM FileEntity f WHERE f.id = :fileId AND f.owner.id = :userId")
    boolean isOwner(String fileId, String userId);
    
    @Query("SELECT f FROM FileEntity f WHERE (:parentId IS NULL AND f.parent.id IS NULL OR f.parent.id = :parentId)")
    List<FileEntity> findByParentId(String parentId);
}