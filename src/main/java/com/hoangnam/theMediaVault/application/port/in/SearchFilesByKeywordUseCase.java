package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.SearchFilesByKeywordQuery;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;

/**
 *
 * 
 */
public interface SearchFilesByKeywordUseCase {
    List<File> execute(SearchFilesByKeywordQuery query);
}
