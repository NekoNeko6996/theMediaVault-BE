package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveAllToTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FileIdAndReason;
import jakarta.transaction.Transactional;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class MoveToTrashService implements MoveAllToTrashUseCase {
    
    private static final Logger logger = Logger.getLogger(MoveToTrashService.class.getName());
    private final FilePersistencePort filePresistencePort;

    @Override
    @Transactional
    public FailedMoveAllToTrashResult execute(MoveAllToTrashCommand command) {
        command.validate();
        
        List<String> validFileIds = new ArrayList();
        List<FileIdAndReason> errors = new ArrayList();
        
        for(String currentFolderId : command.getFileIds()) {
            boolean isOwner = filePresistencePort.isOwner(currentFolderId, command.getOwnerId());
            if(isOwner) {
                validFileIds.add(currentFolderId);
            }
            else {
                errors.add(new FileIdAndReason(currentFolderId, "File not found or you don't have permission to move it to trash."));
            }
        }
        
        if(!validFileIds.isEmpty()) {
            
            try {
                filePresistencePort.moveAllToTrash(command.getFileIds());
            }
            catch(Exception e) {
                logger.severe(e.getMessage());
                throw new DomainException("Error durring drop folders to trash.");
            }
        }
        
        return new FailedMoveAllToTrashResult(errors);
    }
    
}
