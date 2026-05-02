package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper.FileMapper;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.FileEntityRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class FilePersistenceAdapter implements FilePersistencePort {
    
    private final FileEntityRepository fileEntityRepository;
    private final FileMapper fileMapper;
    
    
    // modifying
    @Override
    public void save(File file) {
        fileEntityRepository.save(fileMapper.toEntity(file));
    }
    
    @Override
    public void moveAllToTrash(List<String> fileIds) {
        List<String> ids = fileEntityRepository.findAllChildIds(fileIds);
        
        if(ids != null && !ids.isEmpty()) fileEntityRepository.moveAllToTrash(ids);
    }
    
    @Override
    public void rename(String fileId, String newName) {
        fileEntityRepository.rename(fileId, newName);
    }
    
    @Override
    public void moveFilesToNewDir(List<String> fileIds, String newParentFolderId) {
        fileEntityRepository.moveFiles(fileIds, newParentFolderId);
    }
    
    
    @Override
    public void renameAndMove(String idToMove, String newUniqueName, String targetParentId) {
        fileEntityRepository.renameAndMove(idToMove, newUniqueName, targetParentId);
    }
    
    @Override
    public void toggleStarred(String fileId) {
        fileEntityRepository.toggleStarred(fileId);
    }

    @Override
    public void restoreAllFromTrash(List<String> fileIds) {
        fileEntityRepository.restoreAll(fileIds);
    }
    
    @Override
    public void deleteByIdIn(List<String> fileIds) {
        fileEntityRepository.deleteByIdIn(fileIds);
    }


    
    // query
    @Override
    public Optional<File> findById(String id) {
        return fileEntityRepository.findById(id).map(fileMapper::toDomain);
    }

    @Override
    public boolean findExistsByNameAndParentAndOwner(String name, String parentId, String ownerId) {
        return fileEntityRepository.findByNameAndParentAndOwner(name, parentId, ownerId).isPresent();
    }

    @Override
    public boolean isOwner(String fileId, String userId) {
        return fileEntityRepository.isOwner(fileId, userId);
    }

    @Override
    public List<File> findByParentIdAndOwnerId(String parentId, String ownerId) {
        return fileEntityRepository.findByParentIdAndOwnerId(parentId, ownerId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }
    
    @Override
    public List<File> findExistingFiles(String ownerId, List<String> hashes) {
        return fileEntityRepository.findExistingFilesByHashes(ownerId, hashes).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<File> findByOwnerAndFileIds(String ownerId, List<String> fileId) {
        return fileEntityRepository.findByOwnerAndFileIds(ownerId, fileId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<String> findExistingAndOnerFilesUsingOwnerIdAndFileIds(String ownerId, List<String> FileIds) {
        return fileEntityRepository.findExistingAndOnerFilesUsingOwnerIdAndFileIds(ownerId, FileIds);
    }

    @Override
    public List<String> findAllChildIds(List<String> fileIds) {
        return fileEntityRepository.findAllChildIds(fileIds);
    }

    @Override
    public boolean findExistsByParentIdAndNameAndExtension(String parentId, String name, String extension) {
        return fileEntityRepository.findExistsByParentIdAndNameAndExtension(parentId, name, extension);
    }

    @Override
    public Optional<File> findByIdAndOwnerId(String fileId, String ownerId) {
        return fileEntityRepository.findByIdAndOwnerId(fileId, ownerId).map(fileMapper::toDomain);
    }

    @Override
    public List<File> findAllTrashFilesByOwnerId(String ownerId) {
        return fileEntityRepository.findAllTrashFilesByOwnerId(ownerId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public int countByStoragePath(String storagePath) {
        return fileEntityRepository.countByStoragePath(storagePath);
    }

    @Override
    public List<File> findAllStarredFiles(String ownerId) {
        return fileEntityRepository.findAllStarredFiles(ownerId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<File> findFilesLikeName(String ownerId, String keyword) {
        return fileEntityRepository.findFilesLikeName(ownerId, keyword).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<File> findByOwnerAndFileIdsIncludeTrash(String ownerId, List<String> fileId) {
        return fileEntityRepository.findByOwnerAndFileIdsIncludeTrash(ownerId, fileId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countByStoragePaths(List<String> storagePaths) {
        if(storagePaths == null || storagePaths.isEmpty()) return new HashMap<>();
        
        List<Object[]> results = fileEntityRepository.countByStoragePathsIn(storagePaths);
        Map<String, Long> countMap = new HashMap<>();
        
        for(Object[] row : results) {
            countMap.put((String) row[0], (Long) row[1]);
        }
        
        return countMap;
    }
}
