package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.CheckFilesExistsQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesExistsResult;

/**
 *
 * 
 */
public interface CheckFilesExistsUseCase {
    CheckFilesExistsResult execute(CheckFilesExistsQuery query);
}
