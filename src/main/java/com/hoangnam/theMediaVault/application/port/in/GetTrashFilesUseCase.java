package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetTrashFilesQuery;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;

/**
 *
 * 
 */
public interface GetTrashFilesUseCase {
    List<File> execute(GetTrashFilesQuery query);
}
