package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.ShareFilesCommand;

/**
 *
 * 
 */
public interface ShareFilesUseCase {
    String execute(ShareFilesCommand command);
}
