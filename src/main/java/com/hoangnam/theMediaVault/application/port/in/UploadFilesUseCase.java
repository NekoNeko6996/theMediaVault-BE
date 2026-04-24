package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.in.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.out.FailedFileUploadsResult;


/**
 *
 * 
 */
public interface UploadFilesUseCase {
    FailedFileUploadsResult execute(UploadFilesCommand command);
}
