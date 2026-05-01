package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.HardDeleteFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedHardDeleteFilesResult;

/**
 *
 * 
 */
public interface HardDeleteFilesUseCase {
    FailedHardDeleteFilesResult execute(HardDeleteFilesCommand command);
}
