package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.FilePermission;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class ShareFilesCommand implements ApplicationDTOCommand{
    String ownerId;
    List<String> fileToShareIds;
    List<String> userToShareIds;
    FilePermission permission;
    LocalDate expiration;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) throw new DomainException("Invalid Owner");
        if(expiration.isBefore(LocalDate.now())) throw new DomainException("The expiration date must be after the current date.");
        if(fileToShareIds.isEmpty()) throw new DomainException("At least one file is required to share.");
        
    }
}
