package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.CreateFolderCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CreateFolderResult;

/**
 *
 * 
 */
public interface CreateFolderUseCase {
    CreateFolderResult execute(CreateFolderCommand command);  
}
