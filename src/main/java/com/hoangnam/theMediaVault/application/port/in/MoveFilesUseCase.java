package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveFilesResult;

/**
 *
 * có thể duy chuyển nhiều foler cùng lúc đến 1 path mới
 */
public interface MoveFilesUseCase {
    FailedMoveFilesResult execute(MoveFilesCommand command);
}
