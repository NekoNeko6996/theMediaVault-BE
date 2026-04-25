package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
@AllArgsConstructor
public class CreateFolderCommand implements ApplicationDTOCommand {
    String ownerId;
    String parentId;
    String folderName;
    
    @Override
    public void validate() {
        if(this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner Id must not be null or empty.");
        }
        
        if(this.folderName == null || this.folderName.trim().isEmpty()) {
            throw new DomainException("folder name must not be null or empty.");
        }
    }
}
