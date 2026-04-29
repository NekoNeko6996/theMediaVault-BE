package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import java.util.Optional;

/**
 *
 * 
 */
public interface FilePersistencePort {
    
    // Modifying
    /**
     * Dành cho việc tạo folder(không liên quan đến cập nhật OWNER)
     * Hàm Upload dành cho file đã được chuyển sang FileAndUserTranSactionPort nên ko dùng hàm này để upload file.
     * @param file 
     */
    void save(File file); 
    void rename(String fileId, String newName);
    void moveAllToTrash(List<String> fileIds);
    void moveFilesToNewDir(List<String> fileIds, String newParentFolderId);
    void renameAndMove(String idToMove, String newUniqueName, String targetParentId);
        
    
    // query
    Optional<File> findById(String id);
    boolean findByNameAndParentAndOwner(String name, String parentId, String ownerId);
    boolean isOwner(String fileId, String ownerId);
    List<File> findByParentIdAndOwnerId(String parentId, String ownerId);
    List<File> findExistingFiles(String ownerId, List<String> hashes);
    List<File> findByOwnerAndFileIds(String ownerId, List<String> fileId);
    List<String> findExistingAndOnerFilesUsingOwnerIdAndFileIds(String ownerId, List<String> FileIds);
    
    /**
     * Tìm và trả về 1 danh sách bao gồm các entity có id được truyền vào cũng với các file or folder con bên trong
     * @param fileIds
     * @return 
     */
    List<String> findAllChildIds(List<String> fileIds);
}
