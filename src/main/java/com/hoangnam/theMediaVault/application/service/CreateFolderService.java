package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.CreateFolderUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.CreateFolderCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CreateFolderResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CreateFolderService implements CreateFolderUseCase {
    
    private final LoadUserPort LoadUserPort;
    private final FilePersistencePort filePersistencePort;

    @Override
    @Transactional
    public CreateFolderResult execute(CreateFolderCommand command) {
        command.validate();
        User owner = LoadUserPort.findById(command.getOwnerId()).orElseThrow(() -> new DomainException("User not found."));
        
        File parent = null;
        String basePath = owner.getRootDir();     // root: user-<id>/
        
        String normalizedParentId = (command.getParentId() == null || command.getParentId().trim().isEmpty()) 
                                 ? null 
                                 : command.getParentId();
        
        if (normalizedParentId != null) {
            parent = filePersistencePort.findById(normalizedParentId).orElseThrow(() -> new DomainException("Parent must be a folder."));
            
            if(!parent.getOwner().getId().equals(owner.getId())) {
                throw new DomainException("You don't have permission to create a folder here.");
            }
            
            if(!parent.isFolder()) {
                throw new DomainException("Parent must be a folder.");
            }
            
            basePath = parent.getStoragePath();
        }
        
        if(filePersistencePort.findByNameAndParentAndOwner(command.getFolderName(), normalizedParentId, owner.getId())) {
            throw new DomainException("Folder name already in this directory.");
        }
        
        String folderStoragePath = basePath + command.getFolderName() + "/";
        
        File newFolder = File.createFolder(owner, parent, command.getFolderName(), folderStoragePath);
        
        filePersistencePort.save(newFolder);
        
        return new CreateFolderResult(
                newFolder.getId(), 
                newFolder.getName(), 
                newFolder.getParent() != null ? newFolder.getParent().getId() : null,
                newFolder.getCreateAt()
        );
    }

}
