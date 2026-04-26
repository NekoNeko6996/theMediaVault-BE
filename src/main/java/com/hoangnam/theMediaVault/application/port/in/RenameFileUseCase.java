package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.RenameFileCommand;

/**
 *
 * 
 */
public interface RenameFileUseCase {
    void execute(RenameFileCommand command);
}
