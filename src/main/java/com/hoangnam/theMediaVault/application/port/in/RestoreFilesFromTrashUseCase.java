package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.RestoreFilesFromTrashCommand;

/**
 *
 * 
 */
public interface RestoreFilesFromTrashUseCase {
    void execute(RestoreFilesFromTrashCommand command); 
}
