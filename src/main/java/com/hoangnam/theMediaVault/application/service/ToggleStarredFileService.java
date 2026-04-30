package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.ToggleStarredFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.ToggleStarredFileCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.ToggleStarredFileResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ToggleStarredFileService implements ToggleStarredFileUseCase {
    
    private static final Logger logger = Logger.getLogger(ToggleStarredFileService.class.getName());
    private final FilePersistencePort filePersistencePort;

    @Override
    @Transactional
    public ToggleStarredFileResult execute(ToggleStarredFileCommand command) {
        command.validate();
        
        File targetFile = filePersistencePort.findByIdAndOwnerId(command.getTargetFileId(), command.getOwnerId())
                .orElseThrow(() -> new DomainException("File not exist or you don't have any permission to starred this file."));
        
        try {
            filePersistencePort.toggleStarred(targetFile.getId());
            return new ToggleStarredFileResult(targetFile.getId(), !targetFile.isStarred());
        }
        catch(Exception e) {
            logger.severe(e.getMessage());
            throw new DomainException("Error during toggle star your file.");
        }
    }

}
