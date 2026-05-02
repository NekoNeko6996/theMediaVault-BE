package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class DuplicateFilesCommand implements ApplicationDTOCommand {
    String ownerId;
    String newParentId;
    String oldParentId;
    List<String> fileIds;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Require Owner.");
        }
        
        if(fileIds == null || fileIds.isEmpty()) {
            throw new DomainException("Require fileIds.");
        }
    }
}
