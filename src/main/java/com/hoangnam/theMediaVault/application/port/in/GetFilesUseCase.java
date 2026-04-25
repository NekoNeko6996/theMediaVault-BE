package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFilesQuery;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;

/**
 *
 * 
 */
public interface GetFilesUseCase {
    List<File> execute(GetFilesQuery query);
}
