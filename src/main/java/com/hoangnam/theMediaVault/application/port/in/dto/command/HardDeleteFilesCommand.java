package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class HardDeleteFilesCommand implements ApplicationDTOCommand {
    String ownerId;
    List<String> fileIdsToDelete;

    @Override
    public void validate() {
        if(this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner Id must not be null or empty.");
        }
        
        if(this.fileIdsToDelete == null || this.fileIdsToDelete.isEmpty()) {
            throw new DomainException("Require at least one file to move it to trash.");
        }
    }
}
