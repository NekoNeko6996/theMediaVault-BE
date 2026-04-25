package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


@Value
@AllArgsConstructor
@Builder
public class MoveAllToTrashCommand {
    String ownerId;
    List<String> fileIds;
    
    public void validate() {
        if(this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner Id must not be null or empty.");
        }
        
        if(this.fileIds == null || this.fileIds.isEmpty()) {
            throw new DomainException("Require at least one file to move it to trash.");
        }
    }
}
