package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository;

import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.FileEntity;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 *
 */
@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, String> {

    // =========================================================================================
    // NHÓM ĐỌC DỮ LIỆU (@Query)
    // =========================================================================================
    @EntityGraph(attributePaths = {"owner"})
    @Override
    Optional<FileEntity> findById(String id);
    
    @Query("""
           SELECT f FROM FileEntity f 
           JOIN FETCH f.owner 
           WHERE f.name = :name
           AND ((:parentId IS NULL AND f.parent.id IS NULL) OR (f.parent.id = :parentId))
           AND f.owner.id = :ownerId
           """)
    Optional<FileEntity> findByNameAndParentAndOwner(
            @Param("name") String name,
            @Param("parentId") String parentId,
            @Param("ownerId") String ownerId);

    @Query("SELECT COUNT(f) > 0 FROM FileEntity f WHERE f.id = :fileId AND f.owner.id = :userId")
    boolean isOwner(@Param("fileId") String fileId, @Param("userId") String userId);

    @Query("""
           SELECT f FROM FileEntity f 
           JOIN FETCH f.owner 
           WHERE (:parentId IS NULL AND f.parent.id IS NULL OR f.parent.id = :parentId) 
           AND f.owner.id = :ownerId AND f.isTrashed = false
           """)
    List<FileEntity> findByParentIdAndOwnerId(
            @Param("parentId") String parentId,
            @Param("ownerId") String ownerId);

    /**
     * Tạo 1 bảng tạm -> đưa ids gốc(các parent) vào bảng tạm trước -> đệ quy
     * tìm con, cháu, chắt đến khi không còn rồi đưa hết vào bảng tạm này Sau đó
     * duyệt trên bản tạm này rồi set trashed = 1 và at = now
     *
     * @param fileIds
     */
    @Query(value = """
                   WITH RECURSIVE FileTree AS (
                       SELECT id FROM files WHERE id IN :fileIds
                       UNION ALL 
                       SELECT f.id FROM files f
                       INNER JOIN FileTree ft ON f.parent_id = ft.id
                   )
                   SELECT id FROM FileTree
                   """, nativeQuery = true)
    List<String> findAllChildIds(@Param("fileIds") List<String> fileIds);

    @Query(value = """
                   SELECT f FROM FileEntity f 
                   JOIN FETCH f.owner 
                   WHERE f.owner.id = :ownerId AND f.isTrashed = false AND f.fileHash IN :hashes
                   """)
    List<FileEntity> findExistingFilesByHashes(
            @Param("ownerId") String ownerId,
            @Param("hashes") List<String> hashes);

    @Query(value = """
                   SELECT f FROM FileEntity f 
                   JOIN FETCH f.owner 
                   WHERE f.owner.id = :ownerId AND f.isTrashed = false AND f.id IN :fileIds
                   """)
    List<FileEntity> findByOwnerAndFileIds(
            @Param("ownerId") String ownerId,
            @Param("fileIds") List<String> fileIds);

    /**
     *
     * @param ownerId
     * @param fileIds
     * @return Danh sách file ids tồn tại và owner hợp lệ
     */
    @Query(value = """
                   SELECT f.id FROM FileEntity f
                   WHERE f.owner.id = :ownerId AND f.id IN :fileIds
                   """)
    List<String> findExistingAndOnerFilesUsingOwnerIdAndFileIds(
            @Param("ownerId") String ownerId,
            @Param("fileIds") List<String> fileIds);

    @Query(value = """
                   SELECT COUNT(f) > 0 FROM FileEntity f
                   WHERE ((:parentId IS NULL AND f.parent.id IS NULL) OR (f.parent.id = :parentId)) 
                   AND f.name = :name AND f.extension = :extension
                   """)
    boolean findExistsByParentIdAndNameAndExtension(
            @Param("parentId") String parentId,
            @Param("name") String name,
            @Param("extension") String extension);

    @Query("SELECT f FROM FileEntity f JOIN FETCH f.owner WHERE f.id = :fileId AND f.owner.id = :ownerId")
    Optional<FileEntity> findByIdAndOwnerId(
            @Param("fileId") String fileId,
            @Param("ownerId") String ownerId);
    
    @Query("SELECT f FROM FileEntity f JOIN FETCH f.owner WHERE f.owner.id = :ownerId AND f.isTrashed = true")
    List<FileEntity> findAllTrashFilesByOwnerId(@Param("ownerId") String ownerId);

    // =========================================================================================
    // NHÓM GHI/CẬP NHẬT DỮ LIỆU (@Modifying + @Transactional)
    // =========================================================================================
    @Transactional
    void deleteByIdIn(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FileEntity f SET f.isTrashed = true, f.trashedAt = CURRENT_TIMESTAMP WHERE f.id IN :ids")
    void moveAllToTrash(@Param("ids") List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FileEntity f SET f.name = :newName WHERE f.id = :fileId")
    void rename(@Param("fileId") String fileId, @Param("newName") String newName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
                   UPDATE files 
                   SET parent_id = :newParentFolderId
                   WHERE id IN :fileIds
                   """, nativeQuery = true)
    void moveFiles(
            @Param("fileIds") List<String> fileIds,
            @Param("newParentFolderId") String newParentFolderId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
                   UPDATE files
                   SET name = :newUniqueName, parent_id = :targetParentId
                   WHERE id = :idToMove
                   """, nativeQuery = true)
    void renameAndMove(
            @Param("idToMove") String idToMove,
            @Param("newUniqueName") String newUniqueName,
            @Param("targetParentId") String targetParentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FileEntity f SET f.isStarred = NOT f.isStarred WHERE f.id = :fileId")
    void toggleStarred(@Param("fileId") String fileId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FileEntity f SET f.isTrashed = false, f.trashedAt = null WHERE f.id IN :ids")
    void restoreAll(@Param("ids") List<String> ids);
}
