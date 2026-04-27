package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import java.util.Optional;

/**
 *
 * 
 */
public interface FilePersistencePort {
    
    /**
     * Dành cho việc tạo folder(không liên quan đến cập nhật OWNER)
     * Hàm Upload dành cho file đã được chuyển sang FileAndUserTranSactionPort nên ko dùng hàm này để upload file.
     * @param file 
     */
    void save(File file); 
    
    Optional<File> findById(String id);
    
    boolean findByNameAndParentAndOwner(String name, String parentId, String ownerId);
    boolean isOwner(String fileId, String ownerId);
    
    List<File> findByParentAndOwnerId(String parentId, String ownerId);
    
    void moveAllToTrash(List<String> fileIds);
    
    void rename(String fileId, String newName);
    
    List<File> findExistingFiles(String ownerId, List<String> hashes);
}
