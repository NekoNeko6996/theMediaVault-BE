package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper.FileMapper;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.FileEntityRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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
    @Transactional
    public void moveAllToTrash(List<String> fileIds) {
        List<String> ids = fileEntityRepository.findAllChildIds(fileIds);
        
        if(ids != null && !ids.isEmpty()) fileEntityRepository.moveAllToTrash(ids);
    }
    
    @Override
    @Transactional
    public void rename(String fileId, String newName) {
        fileEntityRepository.rename(fileId, newName);
    }
    
    @Override
    @Transactional
    public void moveFilesToNewDir(List<String> fileIds, String newParentFolderId) {
        fileEntityRepository.moveFiles(fileIds, newParentFolderId);
    }
    
    
    @Override
    @Transactional
    public void renameAndMove(String idToMove, String newUniqueName, String targetParentId) {
        fileEntityRepository.renameAndMove(idToMove, newUniqueName, targetParentId);
    }


    
    // query
    @Override
    public Optional<File> findById(String id) {
        return fileEntityRepository.findById(id).map(fileMapper::toDomain);
    }

    @Override
    public boolean findByNameAndParentAndOwner(String name, String parentId, String ownerId) {
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
    

}
