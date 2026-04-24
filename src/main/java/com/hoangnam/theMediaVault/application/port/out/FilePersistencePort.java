package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import java.util.Optional;

/**
 *
 * 
 */
public interface FilePersistencePort {
    void save(File file); 
    void saves(List<File> file); 
    
    Optional<File> findById(String id);
    
    boolean findByNameAndParentAndOwner(String name, String parentId, String ownerId);
    boolean isOwner(String fileId, String userId);
    
    List<File> findByParentId(String parentId, String ownerId);
}
