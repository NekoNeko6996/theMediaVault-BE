package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.EmptyTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.EmptyTrashResult;

/**
 *
 * 
 */
public interface EmptyTrashUseCase {
    EmptyTrashResult execute(EmptyTrashCommand command);
}
