package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class CheckFilesExistsQuery implements ApplicationDTOCommand {
    String ownerId;
    List<String> hashes;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Invalid ownerId.");
        }
        
        if(hashes == null || hashes.isEmpty()) {
            throw new DomainException("Files hash and size is require.");
        }
        
        for(String hash : hashes) {
            if(hash == null || hash.trim().isEmpty()) {
                throw new DomainException("Invalid files hash and size.");
            }
        }
    }
    
    
}
