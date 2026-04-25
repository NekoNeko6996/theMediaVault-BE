package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class GetUserProfileQuery implements ApplicationDTOCommand {
    String id;

    @Override
    public void validate() {
        if(this.id == null || this.id.trim().isEmpty()) {
            throw new DomainException("Id must not be null or empty.");
        }
    }
    
    
}
