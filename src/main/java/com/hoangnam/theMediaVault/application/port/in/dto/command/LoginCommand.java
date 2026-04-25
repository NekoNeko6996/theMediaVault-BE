package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class LoginCommand implements ApplicationDTOCommand {
    String username;
    String password;

    @Override
    public void validate() {
        if(this.username == null || this.username.trim().isEmpty()) {
            throw new DomainException("Username must not be null or empty.");
        }
        
        if(this.password == null || this.password.trim().isEmpty()) {
            throw new DomainException("Password must not be null or empty.");
        }
    }
    
    
}
