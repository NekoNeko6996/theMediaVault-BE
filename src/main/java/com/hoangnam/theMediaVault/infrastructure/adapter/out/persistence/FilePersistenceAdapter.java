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

    @Override
    public void save(File file) {
        fileEntityRepository.save(fileMapper.toEntity(file));
    }

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
    public List<File> findByParentAndOwnerId(String parentId, String ownerId) {
        return fileEntityRepository.findByParentAndOwnerId(parentId, ownerId).stream().map(fileMapper::toDomain).collect(Collectors.toList());
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
    public List<File> findExistingFiles(String ownerId, List<String> hashes) {
        return fileEntityRepository.findExistingFilesByHashes(ownerId, hashes).stream().map(fileMapper::toDomain).collect(Collectors.toList());
    }

}
