package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class RestoreFilesFromTrashCommand implements ApplicationDTOCommand {
    
    String ownerId;
    List<String> fileIds;

    @Override
    public void validate() {
        if(this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner Id must not be null or empty.");
        }
        
        if(this.fileIds == null || this.fileIds.isEmpty()) {
            throw new DomainException("Require at least one file to restore it from trash.");
        }
    }

}
