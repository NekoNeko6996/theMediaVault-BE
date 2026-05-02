package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class GetFileInfoQuery implements ApplicationDTOCommand {
    String ownerId;
    String fileId;
    
    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Require Owner.");
        }
        
        if(fileId == null || fileId.trim().isEmpty()) {
            throw new DomainException("Require file id.");
        }
    }
}
