package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.SearchFilesByKeywordUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.SearchFilesByKeywordQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SearchFilesByKeywordService implements SearchFilesByKeywordUseCase {
    
    private final FilePersistencePort filePersistencePort;

    @Override
    public List<File> execute(SearchFilesByKeywordQuery query) {
        query.validate();
        return filePersistencePort.findFilesLikeName(query.getOwnerId(), query.getKeyWord());
    }

}
