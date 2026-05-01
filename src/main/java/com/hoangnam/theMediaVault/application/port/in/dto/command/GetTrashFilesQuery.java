package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class GetTrashFilesQuery implements ApplicationDTOCommand {
    String ownerId;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Require Owner.");
        }
    }
    
    
}
