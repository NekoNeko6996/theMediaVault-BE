package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.CheckFilesCanUploadQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesCanUploadResult;

/**
 *
 * 
 */
public interface CheckFilesCanUploadUseCase {
    CheckFilesCanUploadResult execute(CheckFilesCanUploadQuery query);
}
