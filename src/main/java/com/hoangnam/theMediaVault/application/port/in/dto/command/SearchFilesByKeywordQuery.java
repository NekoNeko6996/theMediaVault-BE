package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class SearchFilesByKeywordQuery implements ApplicationDTOCommand {
    String ownerId;
    String keyWord;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Invalid Owner.");
        }
        
        if(keyWord == null || keyWord.trim().isEmpty()) {
            throw new DomainException("Try typing more than a letter to search.");
        }
    }
    
    
}
