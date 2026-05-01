package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetAllStarredFilesQuery;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;

/**
 *
 * 
 */
public interface GetAllStarredFilesUseCase {
    List<File> execute(GetAllStarredFilesQuery query);
}
