package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;


/**
 *
 * 
 */
public interface UploadFilesUseCase {
    FailedFileUploadsResult execute(UploadFilesCommand command);
}
