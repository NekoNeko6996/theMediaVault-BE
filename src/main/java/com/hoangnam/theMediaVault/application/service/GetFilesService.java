package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.GetFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFilesQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetFilesService implements GetFilesUseCase {
    
    private final FilePersistencePort filePersistencePort;

    @Override
    public List<File> execute(GetFilesQuery query) {
        query.validate();
        
        String parentId = null;
        if(query.getParentId() != null && !query.getParentId().trim().isEmpty()) {
            File parent = filePersistencePort.findById(query.getParentId()).orElseThrow(() -> new DomainException("Parent file not found."));
            parentId = parent.getId();
        }
        
        if(parentId != null && !filePersistencePort.isOwner(parentId, query.getOwnerId())) {
            throw new DomainException("Your don't have any permission to view this folder.");
        }
        
        return filePersistencePort.findByParentIdAndOwnerId(parentId, query.getOwnerId());
    }

}
