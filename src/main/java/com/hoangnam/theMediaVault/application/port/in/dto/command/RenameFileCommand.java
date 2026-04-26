package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class RenameFileCommand implements ApplicationDTOCommand {
    
    String fileId;
    String ownerId;
    String newName;
    
    @Override
    public void validate() {
        if(fileId == null || fileId.trim().isEmpty()) {
            throw new DomainException("Invalid file id.");
        }
        
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Invalid owner or you don't have permission to do this action.");
        }
        
        if(newName == null || newName.trim().isEmpty()) {
            throw new DomainException("New name is require.");
        }
    }

}
