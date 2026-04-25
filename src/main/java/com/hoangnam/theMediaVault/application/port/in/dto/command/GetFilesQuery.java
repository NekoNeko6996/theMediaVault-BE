package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class GetFilesQuery implements ApplicationDTOCommand {
    
    String ownerId;
    String parentId;

    @Override
    public void validate() {
        if(this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner Id must not be null or empty.");
        }
    }
    
}
