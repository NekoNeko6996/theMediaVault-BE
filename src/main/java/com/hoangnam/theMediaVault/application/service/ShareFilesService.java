package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.ShareFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.ShareFilesCommand;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.SharePresistencePort;
import com.hoangnam.theMediaVault.application.port.out.UserPort;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ShareFilesService implements ShareFilesUseCase {
    private final UserPort userPort;
    private final FilePersistencePort filePersistencePort;
    private final SharePresistencePort sharePresistencePort;

    @Override
    public String execute(ShareFilesCommand command) {
        command.validate();
        
        return null;
    }
    
    
}
