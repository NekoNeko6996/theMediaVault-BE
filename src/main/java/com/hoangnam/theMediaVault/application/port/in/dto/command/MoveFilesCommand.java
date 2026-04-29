package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MoveFilesCommand implements ApplicationDTOCommand {
    String ownerId;
    String newParentFolderId;
    
    List<String> filesTobeMove;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Inavlid Owner.");
        }
        
        if(filesTobeMove == null || filesTobeMove.isEmpty()) {
            throw new DomainException("At least one file is needed to move.");
        }
    }
    
    
}
