package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.DuplicateFilesCommand;

/**
 *
 * 
 */
public interface DuplicateFilesUseCase {
    void execute(DuplicateFilesCommand command);
}
