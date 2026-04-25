package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveAllToTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;

/**
 *
 * có thể drop nhiều foler cùng lúc
 */
public interface MoveAllToTrashUseCase {
    FailedMoveAllToTrashResult execute(MoveAllToTrashCommand command);
}
