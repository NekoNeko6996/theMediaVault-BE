package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ToggleStarredFileCommand implements ApplicationDTOCommand {
    String ownerId;
    String targetFileId;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Invalid owner.");
        }
        
        if(targetFileId == null || targetFileId.trim().isEmpty()) {
            throw new DomainException("Require target file.");
        }
    }
}
