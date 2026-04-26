package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.RenameFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RenameFileCommand;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RenameFileService implements RenameFileUseCase {
    
    private final FilePersistencePort filePersistencePort;

    @Override
    @Transactional
    public void execute(RenameFileCommand command) {
        command.validate();
        
        File file = filePersistencePort.findById(command.getFileId()).orElseThrow(() -> new DomainException("Invalid file."));
        
        if(!file.getOwner().getId().equals(command.getOwnerId())) {
            throw new DomainException("You don't have any permission to do this acction.");
        }
        
        if(file.getName().equals(command.getNewName())) {
            throw new DomainException("The new name needs to be different from the old name.");
        }
        
        try {
            filePersistencePort.rename(command.getFileId(), command.getNewName());
        }
        catch(Exception e) {
            throw new DomainException("Error durring rename your file.");
        }
    }

}
