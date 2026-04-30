package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.ToggleStarredFileCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.ToggleStarredFileResult;

/**
 *
 * 
 */
public interface ToggleStarredFileUseCase {
    ToggleStarredFileResult execute(ToggleStarredFileCommand command);
}
