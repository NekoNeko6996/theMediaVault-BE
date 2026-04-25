package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveAllToTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;

@RequiredArgsConstructor
public class MoveToTrashService implements MoveAllToTrashUseCase {
    
    private final FilePersistencePort filePresistencePort;
    private final LoadUserPort loadUserPort;

    @Override
    public FailedMoveAllToTrashResult execute(MoveAllToTrashCommand command) {
        command.validate();
        
        User owner = loadUserPort.findById(command.getOwnerId()).orElseThrow(() -> new DomainException("User not found."));
        
        List<String> validFileIds = new ArrayList();
        List<FailedMoveAllToTrashResult.MoveToTrashError> error = new ArrayList();
        
        for(String currentFolderId : command.getFileIds()) {
            boolean isOwner = filePresistencePort.isOwner(currentFolderId, owner.getId());
            if(isOwner) {
                validFileIds.add(currentFolderId);
            }
            else {
                error.add(new FailedMoveAllToTrashResult.MoveToTrashError(
                        currentFolderId, 
                        "File not found or you don't have permission to move it to trash."
                ));
            }
        }
        
        if(!validFileIds.isEmpty()) {
            
            try {
                filePresistencePort.moveAllToTrash(command.getFileIds());
            }
            catch(Exception e) {
                throw new DomainException("Error durring drop folders to trash.");
            }
        }
        
        return new FailedMoveAllToTrashResult(error);
    }
    
}
