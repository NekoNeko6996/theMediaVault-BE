package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.Value;


@Value
public class RegisterCommand implements ApplicationDTOCommand {
    String username;
    String email;
    String password;

    @Override
    public void validate() {
        if(this.username == null || this.username.trim().isEmpty()) {
            throw new DomainException("Username Id must not be null or empty.");
        }
        
        if(this.email == null || this.email.trim().isEmpty()) {
            throw new DomainException("Email Id must not be null or empty.");
        }
        
        if(this.password == null || this.password.trim().isEmpty()) {
            throw new DomainException("Password Id must not be null or empty.");
        }
    }
}
