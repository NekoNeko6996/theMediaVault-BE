package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.in.CreateFolderCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.out.CreateFolderResult;

/**
 *
 * 
 */
public interface CreateFolderUseCase {
    CreateFolderResult execute(CreateFolderCommand command);  
}
